package com.web.catsupplies.order.application;

import com.web.catsupplies.order.domain.Order;
import com.web.catsupplies.order.domain.OrderItem;
import com.web.catsupplies.order.domain.OrderStatus;
import com.web.catsupplies.order.repository.OrderRepository;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.product.repository.ProductRepository;
import com.web.catsupplies.user.domain.User;
import com.web.catsupplies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public void createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 로그인하지 않은 상태입니다."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("제품이 존재하지 않습니다."));

        OrderItem orderItem = OrderItem.of(product, request.getQuantity());

        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PAID)
                .totalPrice(orderItem.getTotalPrice())
                .build();

        order.addOrderItem(orderItem);

        orderRepository.save(order);
    }
}
