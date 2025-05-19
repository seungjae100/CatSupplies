package com.web.catsupplies.payment.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "주문 ID는 필수입니다.")
    @Schema(description = "주문 기본키를 나타냅니다.")
    private Long orderId; // 주문 상품

}
