package com.web.catsupplies.order.domain;

import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_item")
public class OrderItem {

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
