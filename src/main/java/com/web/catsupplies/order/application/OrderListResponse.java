package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderListResponse {

    @Schema(description = "주문한 제품의 기본키를 나타냅니다.")
    private Long orderId; // 주문

    @Schema(description = "주문한 제품의 총 가격을 나타냅니다.", example = "235000")
    private int totalPrice; // 총 가격 합계

    @Schema(description = "주문의 상태를 나타냅니다.", example = "PAID")
    private String orderStatus; // 주문 상태

    @Schema(description = "주문을 작성한 시간을 나타냅니다.", example = "2025.01.01, 13:51:23")
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
