package com.web.catsupplies.order.application;

import com.web.catsupplies.common.exception.UnauthenticatedException;
import com.web.catsupplies.order.domain.Order;
import com.web.catsupplies.order.domain.OrderItem;
import com.web.catsupplies.order.domain.OrderStatus;
import com.web.catsupplies.order.repository.OrderRepository;
import com.web.catsupplies.payment.domain.Payment;
import com.web.catsupplies.payment.domain.PaymentStatus;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.product.repository.ProductRepository;
import com.web.catsupplies.user.domain.User;
import com.web.catsupplies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 주문하기
    @Transactional
    public void createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthenticatedException("유저가 로그인하지 않은 상태입니다."));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("제품이 존재하지 않습니다."));

        OrderItem orderItem = OrderItem.of(product, request.getQuantity());

        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PAID)
                .totalPrice(orderItem.getTotalPrice())
                .build();

        order.addOrderItem(orderItem);

        Payment payment = Payment.builder()
                .order(order)
                .paymentStatus(PaymentStatus.PAID)
                .amount(order.getTotalPrice())
                .build();

        order.setPayment(payment);

        orderRepository.save(order);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문내역이 없습니다."));

        // 주문자 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 주문만 취소할 수 있습니다.");
        }

        // 이미 결제 되었거나 취소된 주문은 취소가 불가
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("이미 결제 되었거나 취소된 주문입니다.");
        }

        // 취소로 상태 변경
        order.cancel();
    }

    // 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderListResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(OrderListResponse::from)
                .collect(Collectors.toList());
    }
}
