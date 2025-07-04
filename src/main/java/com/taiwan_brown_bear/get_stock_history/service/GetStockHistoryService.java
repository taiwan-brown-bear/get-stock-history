package com.taiwan_brown_bear.get_stock_history.service;

import com.taiwan_brown_bear.get_stock_history.dao.StockHistoryDAO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.repository.GetStockHistoryRepository;
import com.taiwan_brown_bear.get_stock_history.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetStockHistoryService {

    @Autowired
    private GetStockHistoryRepository getStockHistoryRepository;

    public void save(GetStockHistoryResponseDTO getStockHistoryResponseDTO) {
        getStockHistoryRepository.saveAll(FormatUtils.convert(getStockHistoryResponseDTO));
    }
}
