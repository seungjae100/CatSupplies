package com.web.catsupplies.payment.application;

import com.web.catsupplies.order.domain.Order;
import com.web.catsupplies.order.repository.OrderRepository;
import com.web.catsupplies.payment.domain.Payment;
import com.web.catsupplies.payment.domain.PaymentStatus;
import com.web.catsupplies.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        // 1. 주문 존재 확인
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        // 2. Payment 객체 생성 (Mock 결제 성공처리)
        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .paymentStatus(PaymentStatus.PAID)
                .build();

        // 3. 연관관계를 사용한 양반향 매핑
        payment.setOrder(order); // order -> payment 연결

        // 4. 저장
        Payment savedPayment = paymentRepository.save(payment);

        // 5. 응답 DTO 반환
        return PaymentResponse.from(savedPayment);
    }

    // 결제 취소
    @Transactional
    public void cancelPayment(Long paymentId, Long userId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다."));

        if (!payment.getOrder().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인만 결제를 취소할 수 있습니다.");
        }

        payment.cancel(); // 결제의 상태 변경
    }

    // 결제내역 조회하기
    @Transactional(readOnly = true)
    public List<PaymentResponse> getMyPayment(Long userId) {
        List<Payment> payments = paymentRepository.findByOrderUserId(userId);

        return payments.stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
    }
}
