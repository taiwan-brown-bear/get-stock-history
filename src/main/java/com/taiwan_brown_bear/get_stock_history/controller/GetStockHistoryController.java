package com.taiwan_brown_bear.get_stock_history.controller;

import com.taiwan_brown_bear.get_stock_history.dao.StockHistoryDAO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryRequestDTO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.service.GetStockHistoryService;
import com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.impl.NasdaqApiService;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        GetStockHistoryResponseDTO getStockHistoryResponseDTO = null;

        // TODO: finish this one ... and, then, ...
        //       work on sweet spot and, then,
        //       back to here to working spring scheduler and spring batch ...
        //       then, put to resume.
        String note = null;
        List<StockHistoryDAO> stockHistoryBetweenFromDateAdnToDate  = getStockHistoryService.find(fromDate, toDate, stockTicker);
        if(source == null && stockHistoryBetweenFromDateAdnToDate.size() > 2) {// TODO: Note: for the case where fromDate and toDate is one day away or the same date, will not check db here ...
            note = "found in database";
            getStockHistoryResponseDTO = FormatUtils.from(stockHistoryBetweenFromDateAdnToDate);
        } else {// if not found, call the 3rd party API to grab it ...
            try {
                log.info("going to get the history data of \"{}\" between \"{}\" and \"{}\" by 3rd party api, \"{}\"", stockTicker, fromDate, toDate, source);
                if ("nasdaq.com".equalsIgnoreCase(source)) {
                    getStockHistoryResponseDTO = nasdaqApiService.getHistorialQuoteDataForStock(stockTicker, fromDate, toDate);
                    note = "calling nasdap.com";
                }// TODO: add else if later. In the future, might try to add more sources ...
                else {
                    log.warn("Unsupported source, {}. Will use \"nasdaq.com\" as source.");
                    getStockHistoryResponseDTO = nasdaqApiService.getHistorialQuoteDataForStock(stockTicker, fromDate, toDate);
                    note = "calling nasdap.com";
                }
                getStockHistoryService.save(getStockHistoryResponseDTO);// TODO: async call option ... data batch ???
            } catch (Throwable t) {
                log.error("Failed to make the 3rd party call: {}", source, t);
                note = "failed to get it from 3rd party call (" + fromDate + ", " + toDate + ")";
            }
        }

        return ResponseEntity.ok(getStockHistoryResponseDTO != null ?
                getStockHistoryResponseDTO.toBuilder().note(note).build() :
                GetStockHistoryResponseDTO.builder().note(note).build());//note != null ? getStockHistoryResponseDTO : GetStockHistoryResponseDTO.builder().note("N/A").build());
    }
}
