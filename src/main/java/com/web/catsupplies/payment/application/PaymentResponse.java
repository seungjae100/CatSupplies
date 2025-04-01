package com.web.catsupplies.payment.application;

import com.web.catsupplies.payment.domain.Payment;
import com.web.catsupplies.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private int amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paidAt;

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaidAt()
        );
    }
}
