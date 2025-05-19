package com.web.catsupplies.stock.controller;

import com.web.catsupplies.common.jwt.CompanyDetails;
import com.web.catsupplies.stock.application.StockResponse;
import com.web.catsupplies.stock.application.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Stock", description = "재고 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    // 재고 조회
    @Operation(summary = "재고 조회",
               description = "로그인한 회사만 자신이 등록한 제품의 재고를 조회할 수 있습니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @GetMapping("/{productId}")
    public ResponseEntity<StockResponse> getStock(@PathVariable Long productId,
                                                  @AuthenticationPrincipal CompanyDetails companyDetails) {
        return ResponseEntity.ok(stockService.getStock(productId, companyDetails.getCompanyId()));
    }

    // 입고
    @Operation(summary = "재고 입고",
               description = "제품에 대해 재고 입고를 처리합니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @PatchMapping("/{productId}/inbound")
    public ResponseEntity<Void> inboundStock(@PathVariable Long productId,
                                             @RequestParam int amount,
                                             @AuthenticationPrincipal CompanyDetails companyDetails) {
        stockService.inboundStock(productId, amount, companyDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }

    // 출고
    @Operation(summary = "재고 출고",
               description = "제품에 대해 재고 출고를 처리합니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @PatchMapping("/{productId}/outbound")
    public ResponseEntity<Void> outboundStock(@PathVariable Long productId,
                                              @RequestParam int amount,
                                              @AuthenticationPrincipal CompanyDetails companyDetails) {
        stockService.outboundStock(productId, amount, companyDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }

    // 수량 증가
    @Operation(summary = "재고 수량 증가",
               description = "수량을 단순 증가시킵니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @PatchMapping("/{productId}/increase")
    public ResponseEntity<Void> increaseStock(@PathVariable Long productId,
                                              @RequestParam int amount,
                                              @AuthenticationPrincipal CompanyDetails companyDetails) {
        stockService.increaseStock(productId, amount, companyDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }

    // 수량 감소
    @Operation(summary = "재고 수량 감소",
               description = "수량을 단순 감소시킵니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long productId,
                                              @RequestParam int amount,
                                              @AuthenticationPrincipal CompanyDetails companyDetails) {
        stockService.decreaseStock(productId, amount, companyDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }

    // 품절 처리
    @Operation(summary = "품절 처리",
               description = "제품을 품절 상태로 변경합니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @PatchMapping("/{productId}/soldout")
    public ResponseEntity<Void> soldOut(@PathVariable Long productId,
                                        @AuthenticationPrincipal CompanyDetails companyDetails) {
        stockService.soldOut(productId, companyDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }
}
