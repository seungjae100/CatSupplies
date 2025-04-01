package com.web.catsupplies.payment.application;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentRequest {
    // Mock 결제 테스트
    @NotNull(message = "주문 ID는 필수입니다.")
    private Long orderId; // 주문 상품

    @NotNull
    private int amount; // 결제 금액

}
