package com.taiwan_brown_bear.get_stock_history.repository;

import com.taiwan_brown_bear.get_stock_history.dao.StockHistoryDAO;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GetStockHistoryRepository extends CrudRepository<StockHistoryDAO, Integer> {
    List<StockHistoryDAO> findByDateBetween                             (String fromDate, String toDate                    );
    List<StockHistoryDAO> findByDateBetweenAndStockTicker               (String fromDate, String toDate, String stockTicker);
    List<StockHistoryDAO> findByDateBetweenAndStockTickerOrderByDateDesc(String fromDate, String toDate, String stockTicker);

}



