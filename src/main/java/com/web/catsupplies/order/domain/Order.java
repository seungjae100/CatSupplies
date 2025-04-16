package com.web.catsupplies.order.domain;

import com.web.catsupplies.payment.domain.Payment;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import com.web.catsupplies.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {
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

    // 결제와의 조인 메서드
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;


    // 연관관계 메서드
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // Payment 연관관계 메서드
    public void setPayment(Payment payment) {
        this.payment = payment;
        if (payment.getOrder() != this) {
            payment.setOrder(this);
        }
    }

    // 주문 생성 (정적 메서드)
    public static Order create(User user, OrderItem orderItem, Payment payment) {
        Order order = new Order();
        order.user = user;
        order.orderStatus = OrderStatus.PAID;
        order.totalPrice = orderItem.getTotalPrice();
        order.addOrderItem(orderItem);
        order.setPayment(payment);
        return order;
    }

    // 주문 취소
    public void cancel() {
        this.orderStatus = OrderStatus.CANCELLED;
    }

}
