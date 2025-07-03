package com.taiwan_brown_bear.get_stock_history.dto;

import lombok.Getter;

@Getter
public class GetStockHistoryRequestDTO {
    private String source;
    private String stockTicker;
    private String fromDate;// expecting that the date will look like "20250528"
    private String toDate;//   expecting that the date will look like "20250628"
}
