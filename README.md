# Avaiable APIs:

request #1:

    curl --location --request GET 'http://localhost:8080/get/stock-history' \
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

request #2:


response #2:
