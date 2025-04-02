package com.web.catsupplies.payment.controller;

import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.payment.application.PaymentRequest;
import com.web.catsupplies.payment.application.PaymentResponse;
import com.web.catsupplies.payment.application.PaymentService;
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

@Tag(name = "Payment", description = "결제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    // 결제 생성
    @Operation(
            summary = "결제",
            description = "JWT 인증필요, 사용자 결제가능",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody @Valid PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity.ok(response);
    }

    // 결제 취소
    @Operation(
            summary = "결제취소",
            description = "JWT 인증필요, 사용자 결제취소가능",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PatchMapping("/{paymentId}/cancel")
    public ResponseEntity<?> cancelPayment(@PathVariable Long paymentId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        paymentService.cancelPayment(paymentId, userId);
        return ResponseEntity.ok(Map.of("message", "결제가 취소되었습니다."));
    }

    // 결제 내역 조회
    @Operation(
            summary = "결제목록조회",
            description = "JWT 인증필요, 사용자 결제내역  확인가능",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayment(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();

        List<PaymentResponse> response = paymentService.getMyPayment(userId);

        return ResponseEntity.ok(response);
    }
}
