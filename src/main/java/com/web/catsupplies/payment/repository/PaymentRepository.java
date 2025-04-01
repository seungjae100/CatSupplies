package com.web.catsupplies.payment.repository;

import com.web.catsupplies.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 유저의 주문한 모든 결제내역을 가져오기
    List<Payment> findByOrderUserId(Long userId);
}
