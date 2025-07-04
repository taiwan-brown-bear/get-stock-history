package com.taiwan_brown_bear.get_stock_history.service.impl;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.dto.NasdaqApiResponseDTO;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NasdaqApiService {

    private final RestClient restClient;

    public NasdaqApiService() {
        this.restClient = RestClient.builder().baseUrl("https://api.nasdaq.com").build();
    }

    public GetStockHistoryResponseDTO getHistorialQuoteDataForStock(String stockTicker, String fromDate, String toDate) {

        String uri = String.format(
                "/api/quote/%s/historical?assetclass=etf&fromdate=%s&limit=9999&todate=%s&random=46",
                stockTicker,
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(fromDate),
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(toDate));// e.g., "/api/quote/SPHD/historical?assetclass=etf&fromdate=2025-05-28&limit=9999&todate=2025-06-28&random=46"

        NasdaqApiResponseDTO nasdaqApiResponseDTO = restClient.get()
                .uri(uri)
                .retrieve()
                .body(NasdaqApiResponseDTO.class);

        return FormatUtils.from(nasdaqApiResponseDTO);
    }

}
