package com.web.catsupplies.stockhistory.application;

import com.web.catsupplies.stock.domain.StockStatus;
import com.web.catsupplies.stockhistory.domain.StockHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StockHistoryResponse {

    private StockStatus stockStatus;

    private int quantityChange;

    private LocalDateTime createdAt;

    public static StockHistoryResponse from(StockHistory stockHistory) {
        return StockHistoryResponse.builder()
                .stockStatus(stockHistory.getStockStatus())
                .quantityChange(stockHistory.getQuantityChange())
                .createdAt(stockHistory.getCreatedAt())
                .build();
    }
}
