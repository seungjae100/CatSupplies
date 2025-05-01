package com.web.catsupplies.stockhistory.controller;

import com.web.catsupplies.common.jwt.CompanyDetails;
import com.web.catsupplies.stockhistory.application.StockHistoryResponse;
import com.web.catsupplies.stockhistory.application.StockHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "StockHistory", description = "재고 변경 이력 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks/history")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    // 재고 이력 조회
    @Operation(summary = "재고 이력 조회",
               description = "해당 제품의 재고 변경 이력을 조회합니다.",
               security = @SecurityRequirement(name = "jwtAuth"))
    @GetMapping("/bystock/{stockId}")
    public ResponseEntity<List<StockHistoryResponse>> getStockHistory(@PathVariable Long stockId,
                                                                      @AuthenticationPrincipal CompanyDetails companyDetails) {
        List<StockHistoryResponse> response = stockHistoryService.getHistory(stockId, companyDetails.getCompanyId());
        return ResponseEntity.ok(response);
    }
}
