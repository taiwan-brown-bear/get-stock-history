package com.taiwan_brown_bear.get_stock_history.controller;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryRequestDTO;
import com.taiwan_brown_bear.get_stock_history.service.impl.NasdaqApiService;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/get/stock-history")
public class GetStockHistoryController {

    @Autowired
    private NasdaqApiService nasdaqApiService;

    @GetMapping
    public ResponseEntity<String> get(@RequestBody GetStockHistoryRequestDTO getStockHistoryRequestDTO)
    {
        final String source      = getStockHistoryRequestDTO.getSource();
        final String stockTicker = getStockHistoryRequestDTO.getStockTicker().toUpperCase();
        final String fromDate    = getStockHistoryRequestDTO.getFromDate();
        final String toDate      = getStockHistoryRequestDTO.getToDate();

        log.info("going to get the history data of \"{}\" between \"{}\" and \"{}\" from \"{}\"", stockTicker, fromDate, toDate, source);

        String response = null;
        if("nasdaq.com".equalsIgnoreCase(source)){
            response = nasdaqApiService.getHistorialQuoteDataForStock(stockTicker, fromDate, toDate);
        }
        // TODO: In the future, might add more sources ...
        else
        {
            log.warn("Unsupported source, {}. Will use \"nasdaq.com\" as source.");
            response = nasdaqApiService.getHistorialQuoteDataForStock(stockTicker, fromDate, toDate);
        }

        return ResponseEntity.ofNullable(FormatUtils.prettyPrintJsonString(response));
    }

}
