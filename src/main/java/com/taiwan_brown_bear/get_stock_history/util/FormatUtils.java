package com.taiwan_brown_bear.get_stock_history.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiwan_brown_bear.get_stock_history.dao.StockHistoryDAO;
import com.taiwan_brown_bear.get_stock_history.dto.GetStockHistoryResponseDTO;
import com.taiwan_brown_bear.get_stock_history.dto.NasdaqApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FormatUtils {

    public static String convert_from_yyyyMMdd_to_yyyy_dash_MM_dash_dd(String yyyyMMdd){// e.g., "20250528" -> "2025-05-28"

        DateTimeFormatter inputFormatter  = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(yyyyMMdd, inputFormatter);

        // Format the LocalDate object into the desired output format
        return date.format(outputFormatter);
    }

    public static String convert_from_MM_slash_dd_slash_yyyy_to_yyyyMMdd(String MM_slash_dd_slash_yyyy){
        DateTimeFormatter inputFormatter  = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate date = LocalDate.parse(MM_slash_dd_slash_yyyy, inputFormatter);

        // Format the LocalDate object into the desired output format
        return date.format(outputFormatter);
    }

    public static String convert_from_yyyy_dash_MM_dash_dd_to_yyyyMMdd(String yyyy_dash_MM_dash_dd){// e.g., "2025-05-28" -> "20250528"

        DateTimeFormatter inputFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate date = LocalDate.parse(yyyy_dash_MM_dash_dd, inputFormatter);

        // Format the LocalDate object into the desired output format
        return date.format(outputFormatter);
    }

    public static String beautify_json_string(String json_string) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json_string);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            log.error("Failed to beautify json, {}.", json_string, e);
        }
        return json_string;
    }

    private static String toCsvRow(String symbol, String date, String close, String vol, String open, String high, String low) {
        return String.join(",  ",
                padSpaceToTheLeft(symbol),
                padSpaceToTheLeft(date),
                padSpaceToTheLeft(close),
                padSpaceToTheLeft(vol),
                padSpaceToTheRight(open),
                padSpaceToTheRight(high),
                padSpaceToTheRight(low)
        );
    }

    public static GetStockHistoryResponseDTO from(NasdaqApiResponseDTO nasdaqApiResponseDTO){
        List<String> stockHistory = new ArrayList<>();
        for(NasdaqApiResponseDTO.Row row : nasdaqApiResponseDTO.data.tradesTable.rows){
            stockHistory.add(
                    toCsvRow(
                            nasdaqApiResponseDTO.data.symbol,
                            convert_from_MM_slash_dd_slash_yyyy_to_yyyyMMdd(row.date),
                            row.close,
                            row.volume.replaceAll(",", ""),
                            row.myopen,
                            row.high,
                            row.low
                    )
            );
        }
        return GetStockHistoryResponseDTO.builder().stockHistoryCsv_ticker_date_close_vol_open_high_low(stockHistory).build();
    }

    public static GetStockHistoryResponseDTO from(List<StockHistoryDAO> stockHistoryDAOList){
        List<String> ticker_date_close_vol_open_high_low = new ArrayList<>();
        for(StockHistoryDAO stockHistoryDAO : stockHistoryDAOList){
            ticker_date_close_vol_open_high_low.add(toCsvRow(
                    stockHistoryDAO.getStockTicker(),
                    stockHistoryDAO.getDate(),
                    stockHistoryDAO.getClose().toString(),
                    stockHistoryDAO.getVolume().toString(),
                    stockHistoryDAO.getOpen().toString(),
                    stockHistoryDAO.getHigh().toString(),
                    stockHistoryDAO.getLow().toString()
            ));
        }
        return GetStockHistoryResponseDTO.builder().stockHistoryCsv_ticker_date_close_vol_open_high_low(ticker_date_close_vol_open_high_low).build();
    }

    public static List<StockHistoryDAO> from(GetStockHistoryResponseDTO getStockHistoryResponseDTO) {
        List<StockHistoryDAO> ret = new ArrayList<>();
        for (String rowInCsv : getStockHistoryResponseDTO.getStockHistoryCsv_ticker_date_close_vol_open_high_low()) {
            String[] date_close_vol_open_high_low = rowInCsv.split(",");
            String ticker = date_close_vol_open_high_low[0].trim();
            String date   = date_close_vol_open_high_low[1].trim();
            String close  = date_close_vol_open_high_low[2].trim();
            String vol    = date_close_vol_open_high_low[3].trim();
            String open   = date_close_vol_open_high_low[4].trim();
            String high   = date_close_vol_open_high_low[5].trim();
            String low    = date_close_vol_open_high_low[6].trim();
            StockHistoryDAO stockHistoryDAO = StockHistoryDAO.builder()
                    .stockTicker(ticker)
                    .date       (date)
                    .close      (Double.parseDouble(close))
                    .volume     (Double.parseDouble(vol  ))
                    .open       (Double.parseDouble(open ))
                    .high       (Double.parseDouble(high ))
                    .low        (Double.parseDouble(low  ))
                    .build();
            ret.add(stockHistoryDAO);
        }
        return ret;
    }

    public static String padSpaceToTheRight(String originalString){
        int fixedLength = 9;
        // Pad with spaces to the right (left-justify)
        String paddedRight = String.format("%-" + fixedLength + "s", originalString);
        return paddedRight;
    }

    public static String padSpaceToTheLeft(String originalString){
        int fixedLength = 10;
        // Pad with spaces to the left (right-justify)
        String paddedLeft = String.format("%" + fixedLength + "s", originalString);
        return paddedLeft;
    }
}