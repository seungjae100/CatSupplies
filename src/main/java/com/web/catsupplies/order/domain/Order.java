package com.web.catsupplies.order.domain;

import com.web.catsupplies.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order")
public class Order {
    // 기본키
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 주문하는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    // 주문 총액
    private int totalPrice;

    // 주문 제품 상세
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 연관관계 메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }




}
