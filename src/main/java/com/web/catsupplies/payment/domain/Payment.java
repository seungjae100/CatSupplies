package com.web.catsupplies.payment.domain;

import com.web.catsupplies.order.domain.Order;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "payments")
public class Payment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    // 실제 결제 금액
    private int amount;

    // 결제 완료 시간
    private LocalDateTime paidAt;

    // Order 연관관계 메서드
    public void setOrder(Order order) {
        this.order = order;
        if (order.getPayment() != this) {
            order.setPayment(this);
        }
    }

    // 결제 생성
    public static Payment create(Order order, int amount) {
        Payment payment = new Payment();
                payment.order = order;
                payment.amount = amount;
                payment.paymentStatus = PaymentStatus.READY;
                payment.setOrder(order);
            return payment;
    }

    // 결제 성공
    public void completePayment() {
        this.paymentStatus = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    // 결제 취소
    public void cancel() {
        this.paymentStatus = PaymentStatus.CANCEL;
    }




}
