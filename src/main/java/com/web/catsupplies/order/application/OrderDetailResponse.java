package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderDetailResponse {

    private Long orderId;
    private String userName;
    private String orderStatus;
    private int totalPrice;
    private List<OrderItemResponse> items;


    public static OrderDetailResponse from(Order order) {
        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .userName(order.getUser().getName())
                .orderStatus(order.getOrderStatus().name())
                .totalPrice(order.getTotalPrice())
                .items(order.getOrderItems().stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
