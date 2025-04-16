package com.web.catsupplies.stock.application;

import com.web.catsupplies.stock.domain.Stock;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockResponse {

    private Long productId;

    private int quantity;

    public static StockResponse from(Stock stock) {
        return StockResponse.builder()
                .productId(stock.getProduct().getId())
                .quantity(stock.getQuantity())
                .build();
    }

}
