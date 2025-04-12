package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderListResponse {

    private Long orderId; // 주문
    private int totalPrice; // 총 가격 합계
    private String orderStatus; // 주문 상태
    private LocalDateTime createdAt; // 주문한 시간


    public static OrderListResponse from(Order order) {
        return OrderListResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }

}
