package com.taiwan_brown_bear.get_stock_history.service.impl;

import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NasdaqApiService {

    private final RestClient restClient;

    public NasdaqApiService() {
        this.restClient = RestClient.builder().baseUrl("https://api.nasdaq.com").build();
    }

    public String getHistorialQuoteDataForStock(String stockTicker, String fromDate, String toDate) {

        String uri = String.format(
                "/api/quote/%s/historical?assetclass=etf&fromdate=%s&limit=9999&todate=%s&random=46",
                stockTicker,
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_MM_dd(fromDate),
                FormatUtils.convert_from_yyyyMMdd_to_yyyy_MM_dd(toDate));// e.g., "/api/quote/SPHD/historical?assetclass=etf&fromdate=2025-05-28&limit=9999&todate=2025-06-28&random=46"

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(String.class);
    }

}
