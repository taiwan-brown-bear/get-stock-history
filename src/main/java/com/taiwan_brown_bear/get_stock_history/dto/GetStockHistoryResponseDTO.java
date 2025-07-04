package com.taiwan_brown_bear.get_stock_history.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class GetStockHistoryResponseDTO {
    private List<String> stockHistoryCsv_date_close_vol_open_high_low;
}
