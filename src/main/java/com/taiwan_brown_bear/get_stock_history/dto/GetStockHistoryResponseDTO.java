package com.taiwan_brown_bear.get_stock_history.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class GetStockHistoryResponseDTO {
    private List<String> stockHistoryCsv_ticker_date_close_vol_open_high_low;
    private String note;// TODO: for now, just put some note to debugging purpose ...
}




