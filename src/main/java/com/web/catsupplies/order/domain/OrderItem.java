package com.web.catsupplies.order.domain;

import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "order_item")
public class OrderItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 주문에 포함된 주문상품인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // 어떤 제품인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 주문 제품의 수량
    private int quantity;

    // 주문 제품의 가격 ( 가격 * 수량 )
    private int totalPrice;

    // 주문한 제품들의 총 가격을 구하기 위한 메서드 (totalPrice 필드, 가격 * 수량)
    public static OrderItem of(Product product, int quantity) {
        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .totalPrice(product.getPrice() * quantity)
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
