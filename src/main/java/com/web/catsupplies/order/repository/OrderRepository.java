package com.web.catsupplies.order.repository;

import com.web.catsupplies.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 유저가 주문한 목록 조회
    List<Order> findByUserId(Long userId);
}
