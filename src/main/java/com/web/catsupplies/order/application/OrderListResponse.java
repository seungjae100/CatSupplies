package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderListResponse {

    private Long orderId;
    private int totalPrice;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public static OrderListResponse from(Order order) {
        return OrderListResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus().name())
                .createdAt(order.getCreatedAt())
                .items(order.getOrderItems()
                        .stream()
                        .map(OrderItemResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
