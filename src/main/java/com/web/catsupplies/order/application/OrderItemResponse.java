package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    private String productName;
    private int quantity;
    private int price;
    private String productImg;

    public static OrderItemResponse from(OrderItem item) {
        return OrderItemResponse.builder()
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getQuantity())
                .productImg(item.getProduct().getImgUrl())
                .build();
    }
}
