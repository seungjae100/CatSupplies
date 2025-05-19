package com.web.catsupplies.order.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderCreateRequest {

    @NotNull
    @Schema(description = "제품 기본키입니다.")
    private Long productId;

    @Min(1) @NotNull
    @Schema(description = "주문수량입니다.", example = "50")
    private int quantity;



}
