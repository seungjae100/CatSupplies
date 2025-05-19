package com.web.catsupplies.stock.application;

import com.web.catsupplies.stock.domain.Stock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StockResponse {

    @Schema(description = "재고 고유 ID")
    private Long stockId;

    @Schema(description = "제품의 코드를 조회합니다.", example = "TEST-001")
    private Long productId;

    @Schema(description = "제품의 재고수량을 조회합니다.", example = "52")
    private int quantity;

    public static StockResponse from(Stock stock) {
        return StockResponse.builder()
                .stockId(stock.getId())
                .productId(stock.getProduct().getId())
                .quantity(stock.getQuantity())
                .build();
    }

}
