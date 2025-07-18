package com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.impl;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.dto.NasdaqApiResponseDTO;
import com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.ThirdPartyApiService;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NasdaqApiService implements ThirdPartyApiService {

    private final RestClient restClient;

    public NasdaqApiService() {
        this.restClient = RestClient.builder().baseUrl("https://api.nasdaq.com").build();
    }

    public GetStockHistoryResponseDTO getHistorialQuoteDataForStock(String stockTicker, String fromDate, String toDate) {

        String uri = String.format(
                "/api/quote/%s/historical?assetclass=etf&fromdate=%s&limit=9999&todate=%s&random=46",
                stockTicker,
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(fromDate),
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(toDate));

        NasdaqApiResponseDTO nasdaqApiResponseDTO = restClient.get()
                .uri(uri)
                .retrieve()
                .body(NasdaqApiResponseDTO.class);

        GetStockHistoryResponseDTO getStockHistoryResponseDTO = FormatUtils.from(nasdaqApiResponseDTO);
        return getStockHistoryResponseDTO;
    }

}
