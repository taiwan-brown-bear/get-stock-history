Step 1/3: Add the following dependency for MCP server.

	    <properties>
		    <java.version>24</java.version>
	+   	<spring-ai.version>1.0.0</spring-ai.version>
	    </properties>

    + 		<dependency>
    +			<groupId>org.springframework.ai</groupId>
    +			<artifactId>spring-ai-starter-mcp-server</artifactId>
    +		</dependency>

    +  	<dependencyManagement>
    +		<dependencies>
    +			<dependency>
    +				<groupId>org.springframework.ai</groupId>
    +				<artifactId>spring-ai-bom</artifactId>
    +				<version>${spring-ai.version}</version>
    +				<type>pom</type>
    +				<scope>import</scope>
    +			</dependency>
    +		</dependencies>
    +	</dependencyManagement>

Step 2/3: In order to modify the web service to MCP server. The following configurations are added to application.properties.

    spring.ai.mcp.server.type=SYNC
    spring.ai.mcp.server.name=get-stock-history-mcp-server
    spring.ai.mcp.server.version=1.0.0

    spring.main.web-application-type=none
    spring.main.banner-mode=off
    logging.pattern.console=

    logging.file.name=get-stock-history.log

Step 3/3: Once implementing the MCP Server, we need to add the command to start MCP Server to claude_desktop_config.json. (Note: we use Claude Desktop for MCP Client here)

    {
      "mcpServers": {
        "get-stock-history-server": {
          "command": "java",
          "args": [
	        "-jar",
	        "/Users/charles/IdeaProjects/get-stock-history/target/get-stock-history-0.0.1-SNAPSHOT.jar"
          ]
        }
      }
    }

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

# Troubleshooting:

Q: In case you use lombok. When "mvn clean install", if getting Symbol Not Found error about lombok, add the following.
A: After adding the following, the error goes away.

                   <dependency>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
    +                       <scope>provided</scope>
                            <optional>true</optional>
                    </dependency>
                    <dependency>
    @@ -97,6 +98,30 @@
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-maven-plugin</artifactId>
                           </plugin>
    +                       <plugin>
    +                               <groupId>org.apache.maven.plugins</groupId>
    +                               <artifactId>maven-compiler-plugin</artifactId>
    +                               <configuration>
    +                                       <annotationProcessorPaths>
    +                                               <path>
    +                                                       <groupId>org.projectlombok</groupId>
    +                                                       <artifactId>lombok</artifactId>
    +                                               </path>
    +                                       </annotationProcessorPaths>
    +                               </configuration>
    +                       </plugin>
    +                       <plugin>
    +                               <groupId>org.springframework.boot</groupId>
    +                               <artifactId>spring-boot-maven-plugin</artifactId>
    +                               <configuration>
    +                                       <excludes>
    +                                               <exclude>
    +                                                       <groupId>org.projectlombok</groupId>
    +                                                       <artifactId>lombok</artifactId>
    +                                               </exclude>
    +                                       </excludes>
    +                               </configuration>
    +                       </plugin>
                    </plugins>
            </build>

Q: When MCP Client runnning the tool provided by MCP Server, if things go south, where can we start the debugging ?
A: The log will be writen to get-stock-history.log.

For example,

        java.lang.NullPointerException: Cannot invoke "java.util.ArrayList.iterator()" because "nasdaqApiResponseDTO.data.tradesTable.rows" is null
	    at com.taiwan_brown_bear.get_stock_history.util.FormatUtils.from(FormatUtils.java:74) ~[classes/:na]
	    at com.taiwan_brown_bear.get_stock_history.service.thirdpartyapi.impl.NasdaqApiService.getHistorialQuoteDataForStock(NasdaqApiService.java:34) ~[classes/:na]
	    at com.taiwan_brown_bear.get_stock_history.controller.GetStockHistoryController.get(GetStockHistoryController.java:52) ~[classes/:na]
	    at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
	    at java.base/java.lang.reflect.Method.invoke(Method.java:565) ~[na:na]

