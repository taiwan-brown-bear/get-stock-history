package com.taiwan_brown_bear.get_stock_history.service;

import com.taiwan_brown_bear.get_stock_history.dao.StockHistoryDAO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.repository.GetStockHistoryRepository;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetStockHistoryService {

    @Autowired
    private GetStockHistoryRepository getStockHistoryRepository;

    public void save(GetStockHistoryResponseDTO getStockHistoryResponseDTO) {
        List<StockHistoryDAO> stockHistoryDAOList = FormatUtils.from(getStockHistoryResponseDTO);
        try {
            getStockHistoryRepository.saveAll(stockHistoryDAOList);
        } catch (Exception e) {
            log.info("Failed to save all due to {}", e.getCause(), e);
        }
    }

    public List<StockHistoryDAO> find(String fromDate, String toDate, String stockTicker) {
        return getStockHistoryRepository.findByDateBetweenAndStockTicker(fromDate, toDate, stockTicker);
        //return getStockHistoryRepository.findByDateBetweenAndStockTickerOrderByDateDesc(fromDate, toDate, stockTicker);
    }
}
