package com.web.catsupplies.order.controller;

import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.order.application.OrderCreateRequest;
import com.web.catsupplies.order.application.OrderListResponse;
import com.web.catsupplies.order.application.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        orderService.createOrder(userId, request);

        return ResponseEntity.ok(Map.of("message", "주문이 완료되었습니다."));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(Map.of("message", "주문이 취소되었습니다."));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<OrderListResponse> response = orderService.getMyOrders(userId);
        return ResponseEntity.ok(response);
    }


}
