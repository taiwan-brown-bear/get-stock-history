# Avaiable APIs:

request #1:

curl --location --request GET 'http://localhost:8080/get/stock-history' \
--header 'Content-Type: application/json' \
--data '{
    "stockTicker": "SPYD",
    "fromDate"   : "20250701",
    "toDate"     : "20250704",
    "source"     : "nasdap.com"
}'

response #1:

{
    "stockHistoryCsv_ticker_date_close_vol_open_high_low": [
        "      SPYD,    20250703,       43.51,      683499,  43.49    ,  43.655   ,  43.415   ",
        "      SPYD,    20250702,       43.43,     1141983,  43.155   ,  43.48    ,  43.0604  ",
        "      SPYD,    20250701,       43.15,     1465732,  42.44    ,  43.47    ,  42.43    "
    ],
    "note": "calling nasdap.com"
}

request #2:


response #2:
