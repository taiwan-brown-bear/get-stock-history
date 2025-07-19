package com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.impl;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.dto.NasdaqApiResponseDTO;
import com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.ThirdPartyApiService;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class NasdaqApiService implements ThirdPartyApiService {

    private final RestClient restClient;

    public NasdaqApiService() {
        this.restClient = RestClient.builder().baseUrl("https://api.nasdaq.com").build();
    }

    public GetStockHistoryResponseDTO getHistorialQuoteDataForStock(String stockTicker, String fromDate, String toDate) {

        String assetclass = "etf";
        NasdaqApiResponseDTO nasdaqApiResponseDTO = getNasdaqApiResponseDTO(stockTicker, fromDate, toDate, assetclass);

        if(nasdaqApiResponseDTO.data == null){// Note: probably assetclass is not etf. let's try stocks !!!
            assetclass = "stocks";
            nasdaqApiResponseDTO = getNasdaqApiResponseDTO(stockTicker, fromDate, toDate, assetclass);
            log.info("The Stock Ticker, {}, has asset class as INDIVIDUAL STOCK.", stockTicker);
        } else {
            log.info("The Stock Ticker, {}, has asset class as ETF.", stockTicker);
        }

        GetStockHistoryResponseDTO getStockHistoryResponseDTO = FormatUtils.from(nasdaqApiResponseDTO);
        return getStockHistoryResponseDTO;
    }

    private NasdaqApiResponseDTO getNasdaqApiResponseDTO(String stockTicker, String fromDate, String toDate, String assetclass) {

        String uri = String.format(
                "/api/quote/%s/historical?assetclass=%s&fromdate=%s&limit=9999&todate=%s&random=46",
                stockTicker,
                assetclass,
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(fromDate),
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(toDate));

        NasdaqApiResponseDTO nasdaqApiResponseDTO = restClient.get()
                .uri(uri)
                .retrieve()
                .body(NasdaqApiResponseDTO.class);

        return nasdaqApiResponseDTO;
    }

}
