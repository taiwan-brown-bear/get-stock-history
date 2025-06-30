package com.taiwan_brown_bear.get_stock_history.controller;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryRequestDTO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/get/stock-history")
public class GetStockHistoryController {

    @GetMapping
    public ResponseEntity<GetStockHistoryResponseDTO> get(@RequestBody GetStockHistoryRequestDTO getStockHistoryRequestDTO)
    {
        final String source      = getStockHistoryRequestDTO.getSource().toLowerCase();
        final String stockTicker = getStockHistoryRequestDTO.getStockTicker().toUpperCase();
        final String fromDate    = getStockHistoryRequestDTO.getFromDate();
        final String toDate      = getStockHistoryRequestDTO.getToDate();

        log.info("going to get the history data of \"{}\" between \"{}\" and \"{}\" from \"{}\"", stockTicker, fromDate, toDate, source);
        GetStockHistoryResponseDTO getStockHistoryResponseDTO = null;
        return ResponseEntity.ok(getStockHistoryResponseDTO);
    }

}
