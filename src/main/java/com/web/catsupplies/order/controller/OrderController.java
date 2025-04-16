package com.web.catsupplies.order.controller;

import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.order.application.OrderCreateRequest;
import com.web.catsupplies.order.application.OrderListResponse;
import com.web.catsupplies.order.application.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Order", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @Operation(
            summary = "주문하기",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderCreateRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        orderService.createOrder(userId, request);

        return ResponseEntity.ok(Map.of("message", "주문이 완료되었습니다."));
    }

    // 주문 취소
    @Operation(
            summary = "주문취소",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(Map.of("message", "주문이 취소되었습니다."));
    }

    // 주문 목록조회
    @Operation(
            summary = "주문목록조회",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("")
    public ResponseEntity<?> getMyOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<OrderListResponse> response = orderService.getMyOrders(userId);
        return ResponseEntity.ok(response);
    }

    // 주문 상세 조회
    @Operation(
            summary = "주문상세조회",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long orderId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(orderService.getOrderDetail(orderId, userId));
    }
}
