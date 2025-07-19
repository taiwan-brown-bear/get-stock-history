package com.taiwan_brown_bear.get_stock_history.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


public class NasdaqApiResponseDTO {// TODO: Note: Computer Generated Code. I just renamed it and put the rest as static inner class here ...
    //
    // Note: use (online tool)         https://json2csharp.com/code-converters/json-to-pojo
    //       and (nasdaq.com response) https://taiwan-brown-bear.atlassian.net/wiki/spaces/~712020271ff02da58c4d98be1a8bde29b377b6/pages/19431572/Public+get-stock-history+for+the+first+2+APIs
    //       to generate this class
    //
    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
    // import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
    /* ObjectMapper om = new ObjectMapper();
       Root root = om.readValue(myJsonString, Root.class); */
    public Data data;
    public Object message;
    public Status status;

    public static class Data{
        public String symbol;
        public int totalRecords;
        public TradesTable tradesTable;
    }

    public static class Headers{
        public String date;
        public String close;
        public String volume;
        @JsonProperty("open")
        public String myopen;
        public String high;
        public String low;
    }

    public static class Row{
        public String date;
        public String close;
        public String volume;
        @JsonProperty("open")
        public String myopen;
        public String high;
        public String low;
    }

    public static class Status{
        public int rCode;
        public Object bCodeMessage;
        public Object developerMessage;
    }

    public static class TradesTable{
        public Object asOf;
        public Headers headers;
        public ArrayList<Row> rows;
    }
}




