Step 1: In order to modify the web service to MCP server. The following are added to application.properties.

    spring.ai.mcp.server.type=SYNC
    spring.ai.mcp.server.name=get-stock-history-mcp-server
    spring.ai.mcp.server.version=1.0.0

    spring.main.web-application-type=none
    spring.main.banner-mode=off
    logging.pattern.console=

    logging.file.name=get-stock-history.log

Step 2: 


-------

# Note: The following 2 REST APIs below are disabled since web application type is set to "none" now in application.properties

    spring.main.web-application-type=none

# 2 Avaiable APIs:

    -----------------------------------------------------------------------------------------------

    request #1: (with "source" specified, will try to get it from nasdap.com)

    curl --location --request GET 'http://localhost:8080/stock-history' \
    --header 'Content-Type: application/json' \
    --data '{
        "stockTicker": "QQQ",
        "fromDate"   : "20250701",
        "toDate"     : "20250704",
        "source"     : "nasdap.com"
    }'

    response #1:

    {
        "stockHistoryCsv_ticker_date_close_vol_open_high_low": [
            "       QQQ,    20250703,      556.22,    26443520,  553.18   ,  557.20   ,  553.18   ",
            "       QQQ,    20250702,      550.80,    36538280,  546.16   ,  551.00   ,  546.12   ",
            "       QQQ,    20250701,      546.99,    56166740,  549.73   ,  550.71   ,  544.66   "
        ],
        "note": "calling nasdap.com"
    }

    -----------------------------------------------------------------------------------------------

    request #2: (without "source" specified, will get it from db)

    curl --location --request GET 'http://localhost:8080/stock-history' \
    --header 'Content-Type: application/json' \
    --data '{
        "stockTicker": "QQQ",
        "fromDate"   : "20250701",
        "toDate"     : "20250704"
    }'

    response #2:

    {
        "stockHistoryCsv_ticker_date_close_vol_open_high_low": [
            "       QQQ,    20250701,      546.99,  5.616674E7,  549.73   ,  550.71   ,  544.66   ",
            "       QQQ,    20250702,       550.8,  3.653828E7,  546.16   ,  551.0    ,  546.12   ",
            "       QQQ,    20250703,      556.22,  2.644352E7,  553.18   ,  557.2    ,  553.18   "
        ],
        "note": "found in database"
    }

    -----------------------------------------------------------------------------------------------
