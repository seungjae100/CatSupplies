package com.web.catsupplies.order.application;

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
    private Long productId;

    @Min(1) @NotNull
    private int quantity;



}
