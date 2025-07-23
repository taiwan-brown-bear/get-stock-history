package com.taiwan_brown_bear.get_stock_history.service;

import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.impl.NasdaqApiService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BuySellStockService {

    @Autowired
    private NasdaqApiService nasdaqApiService;

    @Tool(name = "tbb_getStockPrice",
    description = "ask for share price of stock. Specify stockTicker and date")
    public String getStockPrice(String stockTicker, String date_in_format_of_yyyyMMdd){
        GetStockHistoryResponseDTO getStockHistoryResponseDTO =
                nasdaqApiService.getHistorialQuoteDataForStock(
                        stockTicker,
                        date_in_format_of_yyyyMMdd, // FROM (inclusive)
                        date_in_format_of_yyyyMMdd);// TO (inclusive)
        List<String> records = getStockHistoryResponseDTO.getStockHistoryCsv_ticker_date_close_vol_open_high_low();
        if(records != null && records.size() > 0){
            return records.get(0);
        }
        return "couldn't retrieve the share price for " + stockTicker;
    }

    public record StockHoldingRecord(String stockTicker, int quantity) {
    }

    private final Map<String, StockHoldingRecord> stockHoldings = new ConcurrentHashMap<>();

    @Tool(name = "tbb_buyStock",
    description = "buy stock(s) or update its quantity. Specify stockTicker and quantity.")
    public String buyStock(String stockTicker, int quantity){
        if (stockTicker == null || stockTicker.trim().isEmpty() || quantity <= 0) {
            return "Error: Invalid stock ticker, " + stockTicker + ", or quantity, " + quantity + ".";
        }
        stockHoldings.compute(stockTicker.toUpperCase(), (key, existingItem) -> {
            if (existingItem == null) {
                return new StockHoldingRecord(stockTicker.toUpperCase(), quantity);
            } else {
                return new StockHoldingRecord(existingItem.stockTicker(), existingItem.quantity + quantity);
            }
        });
        return "Just bought " + quantity + " share(s) of " + stockTicker.toUpperCase() + ".";
    }

    @Tool(name = "ttb_getStockHoldings",
    description = "Get the current stock holdings. Returns a list of stocks with their stockTickers and quantities.")
    public List<StockHoldingRecord> getStockHoldings(){
        return new ArrayList<>(stockHoldings.values());
    }

    @Tool(name = "ttb_sellStocks",
    description = "sell stock(s) or update its quantity. Specify stockTicker and quantity.")
    public String sellStock(String stockTicker, int quantity){
        if (stockTicker == null || stockTicker.trim().isEmpty() || quantity <= 0) {
            return "Error: Invalid stock ticker, " + stockTicker + ", or quantity, " + quantity + ".";
        }
        String stockTickerUpperCase = stockTicker.toUpperCase();
        StockHoldingRecord stockHoldingRecord = stockHoldings.get(stockTicker);
        if(stockHoldingRecord == null){
            return "Error: You don't own any shares of " + stockTicker + ". You cannot perform this action.";
        }
        int currentHolding = stockHoldingRecord.quantity;
        if(quantity > currentHolding){
            return "Error: You cannot sell more than what you own. (" + stockTickerUpperCase + ": " + quantity + " > " + currentHolding + ").";
        } else if (quantity == currentHolding){
            stockHoldings.remove(stockTickerUpperCase);
            return "You sold all your shares of " + stockTickerUpperCase +
                    " (i.e., sold " + quantity + " shares of "+ stockTickerUpperCase +")";
        } else {
            int newQuantity = currentHolding - quantity;
            stockHoldings.put(stockTickerUpperCase, new StockHoldingRecord(stockTickerUpperCase, newQuantity));
            return "You sold " + quantity + " shares of " + stockTickerUpperCase +
                    " (i.e., you have remaining " + currentHolding + "-" + quantity + "=" + newQuantity + " shares of " + stockTickerUpperCase + ".";
        }
    }
}
