package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderDetailResponse {

    @Schema(description = "주문 기본키입니다.", example = "23")
    private Long orderId;

    @Schema(description = "주문자이름입니다.", example = "김민수")
    private String userName;

    @Schema(description = "주문상태를 나타냅니다.", example = "PAID,PENDING,CANCELLED")
    private String orderStatus;

    @Schema(description = "주문 총액을 나타냅니다.", example = "120,000원")
    private int totalPrice;

    @Schema(description = "주문 상세 내용을 나타냅니다.")
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
