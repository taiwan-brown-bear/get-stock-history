package com.taiwan_brown_bear.get_stock_history.repository;

import com.taiwan_brown_bear.get_stock_history.dao.StockHistoryDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GetStockHistoryRepository extends CrudRepository<StockHistoryDAO, Integer> {

//    // Custom JPQL query to find entities within a date range
//    @Query("SELECT sh FROM stock_history sh WHERE sh.date >= :fromDate AND sh.date <= :toDate ORDER BY sh.date DESC")
//    List<StockHistoryDAO> getStockHistoryInDateRange(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

//    //@Query("SELECT e FROM stock_history e WHERE e.date BETWEEN :startDate AND :endDate")
//    List<StockHistoryDAO> findStockHistorysInDateRange(@Param("startDate") String startDate,
//                                      @Param("endDate") String endDate);

    List<StockHistoryDAO> findByDateBetween(String fromDate, String toDate);

    List<StockHistoryDAO> findByDateBetweenAndStockTicker(String fromDate, String toDate, String stockTicker);

    List<StockHistoryDAO> findByDateBetweenAndStockTickerOrderByDateDesc(String fromDate, String toDate, String stockTicker);

}
