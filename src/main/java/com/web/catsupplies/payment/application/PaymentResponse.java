package com.web.catsupplies.payment.application;

import com.web.catsupplies.payment.domain.Payment;
import com.web.catsupplies.payment.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {

    @Schema(description = "결제 기본키를 나타냅니다.", example = "245")
    private Long paymentId;

    @Schema(description = "실제 마지막 결제 금액을 나타냅니다.", example = "39,900원")
    private int amount;

    @Schema(description = "결제 상태를 나타냅니다.", example = "READY, PAID, FAILED")
    private String paymentStatus;

    @Schema(description = "결제 시간을 나타냅니다.", example = "2025.01.15, 17:23:59")
    private LocalDateTime paidAt;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus().name())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
