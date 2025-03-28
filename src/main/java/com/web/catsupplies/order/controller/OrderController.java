package com.web.catsupplies.order.controller;

import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.order.application.OrderCreateRequest;
import com.web.catsupplies.order.application.OrderService;
import com.web.catsupplies.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest requeset,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        orderService.createOrder(userId, requeset);

        return ResponseEntity.ok(Map.of("message", "주문이 완료되었습니다."));
    }
}
