package com.web.catsupplies.stockhistory.application;

import com.web.catsupplies.stock.domain.StockStatus;
import com.web.catsupplies.stockhistory.domain.StockHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StockHistoryResponse {

    @Schema(description = "제품의 상태를 조회합니다.", example = "INBOUND, OUTBOUND, SOLD_OUT, STOCK_INCREASED, STOCK_DECREASED")
    private StockStatus stockStatus;

    @Schema(description = "변화하는 수량을 조회합니다.", example = "23 개")
    private int quantityChange;

    @Schema(description = "재고변경일을 조회합니다.", example = "2025-04-17T05:00:15.687Z")
    private LocalDateTime createdAt;

    public static StockHistoryResponse from(StockHistory stockHistory) {
        return StockHistoryResponse.builder()
                .stockStatus(stockHistory.getStockStatus())
                .quantityChange(stockHistory.getQuantityChange())
                .createdAt(stockHistory.getCreatedAt())
                .build();
    }
}
