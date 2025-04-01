package com.web.catsupplies.payment.controller;

import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.payment.application.PaymentRequest;
import com.web.catsupplies.payment.application.PaymentResponse;
import com.web.catsupplies.payment.application.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 생성
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody @Valid PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity.ok(response);
    }

    // 결제 취소
    @PatchMapping("/{paymentId}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long paymentId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        paymentService.cancelPayment(paymentId, userId);
        return ResponseEntity.ok(Map.of("message", "결제가 취소되었습니다."));
    }

    // 결제 내역 조회
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayment(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();

        List<PaymentResponse> response = paymentService.getMyPayment(userId);

        return ResponseEntity.ok(response);
    }
}
