package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    @Schema(description = "주문한 제품 이름을 나타냅니다.", example = "강아지꿀사료")
    private String productName;

    @Schema(description = "주문한 제품의 수량을 나타냅니다.", example = "4")
    private int quantity;

    @Schema(description = "주문한 상품의 가격을 나타냅니다.", example = "23000")
    private int price;

    @Schema(description = "주문한 제품의 이미지를 나타냅니다.", example = "제품의 이미지")
    private String productImg;

    public static OrderItemResponse from (OrderItem item) {
        return OrderItemResponse.builder()
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getProduct().getPrice())
                .productImg(item.getProduct().getImgUrl())
                .build();
    }
}
