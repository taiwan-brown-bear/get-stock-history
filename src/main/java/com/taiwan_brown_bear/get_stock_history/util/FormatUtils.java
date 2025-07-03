package com.taiwan_brown_bear.get_stock_history.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class FormatUtils {

    public static String convertDateFormat(String yyyymmddDate){// e.g., "20250528" -> "2025-05-28"

        DateTimeFormatter inputFormatter  = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(yyyymmddDate, inputFormatter);

        // Format the LocalDate object into the desired output format
        return date.format(outputFormatter);
    }

    public static String prettyPrintJsonString(String uglyJsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(uglyJsonString);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            log.error("Failed to beautify json, {}.", uglyJsonString, e);
        }
        return uglyJsonString;
    }
}
