package com.taiwan_brown_bear.get_stock_history.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static GetStockHistoryResponseDTO from(NasdaqApiResponseDTO nasdaqApiResponseDTO){
        List<String> stockHistory = new ArrayList<>();
        for(NasdaqApiResponseDTO.Row row : nasdaqApiResponseDTO.data.tradesTable.rows){
            stockHistory.add(String.join(",  ",
                    padSpaceToTheLeft(convert_from_MM_slash_dd_slash_yyyy_to_yyyyMMdd(row.date)),
                    padSpaceToTheLeft(row.close),
                    padSpaceToTheLeft(row.volume),
                    padSpaceToTheRight(row.myopen),
                    padSpaceToTheRight(row.high),
                    padSpaceToTheRight(row.low)
                    // TODO: so far, I only care about date and close price and, perhaps, vol ...
            ));
        }
        return GetStockHistoryResponseDTO.builder().stockHistoryCsvList(stockHistory).build();
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
