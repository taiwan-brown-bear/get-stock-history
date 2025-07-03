package com.taiwan_brown_bear.get_stock_history.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FormatUtils {

    public static String convert_from_yyyyMMdd_to_yyyy_MM_dd(String yyyyMMdd){// e.g., "20250528" -> "2025-05-28"

        DateTimeFormatter inputFormatter  = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(yyyyMMdd, inputFormatter);

        // Format the LocalDate object into the desired output format
        return date.format(outputFormatter);
    }

    public static String convert_from_yyyy_MM_dd_to_yyyyMMdd(String yyyy_MM_dd){// e.g., "2025-05-28" -> "20250528"

        DateTimeFormatter inputFormatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate date = LocalDate.parse(yyyy_MM_dd, inputFormatter);

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
}
