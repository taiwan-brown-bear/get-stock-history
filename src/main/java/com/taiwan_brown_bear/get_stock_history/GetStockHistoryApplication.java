package com.taiwan_brown_bear.get_stock_history;

import com.taiwan_brown_bear.get_stock_history.service.BuySellStockService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class GetStockHistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetStockHistoryApplication.class, args);
	}

	@Bean
	public List<ToolCallback> tools(BuySellStockService buySellStockService) {
		return List.of(ToolCallbacks.from(buySellStockService));
	}
}

