package com.taiwan_brown_bear.get_stock_history.controller;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryRequestDTO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.service.GetStockHistoryService;
import com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.impl.NasdaqApiService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/get/stock-history")
public class GetStockHistoryController {

    @Autowired
    private GetStockHistoryService getStockHistoryService;

    @Autowired
    private NasdaqApiService nasdaqApiService;

    @GetMapping
    public ResponseEntity<GetStockHistoryResponseDTO> get(@RequestBody GetStockHistoryRequestDTO getStockHistoryRequestDTO)
    {
        final String source      = getStockHistoryRequestDTO.getSource();
        final String stockTicker = getStockHistoryRequestDTO.getStockTicker().toUpperCase();
        final String fromDate    = getStockHistoryRequestDTO.getFromDate();
        final String toDate      = getStockHistoryRequestDTO.getToDate();

        boolean isFoundInDb = false;
        GetStockHistoryResponseDTO getStockHistoryResponseDTO = null;
        // TODO: check the db


        // TODO: if not found, call the 3rd party API
        if(isFoundInDb){

        } else {
            try {
                log.info("going to get the history data of \"{}\" between \"{}\" and \"{}\" by 3rd party api, \"{}\"", stockTicker, fromDate, toDate, source);
                if ("nasdaq.com".equalsIgnoreCase(source)) {
                    getStockHistoryResponseDTO = nasdaqApiService.getHistorialQuoteDataForStock(stockTicker, fromDate, toDate);
                }// TODO: add else if later. In the future, might try to add more sources ...
                else {
                    log.warn("Unsupported source, {}. Will use \"nasdaq.com\" as source.");
                    getStockHistoryResponseDTO = nasdaqApiService.getHistorialQuoteDataForStock(stockTicker, fromDate, toDate);
                }
                getStockHistoryService.save(getStockHistoryResponseDTO);
            } catch (Exception e) {
                log.error("Failed to make the 3rd party call: {}", source, e);
                return ResponseEntity.ofNullable(getStockHistoryResponseDTO);
            }
        }

        return ResponseEntity.ok(getStockHistoryResponseDTO);
    }
}
