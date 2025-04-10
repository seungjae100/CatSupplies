package com.web.catsupplies.product.controller;

import com.web.catsupplies.common.jwt.CompanyDetails;
import com.web.catsupplies.product.application.*;
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

@Tag(name = "Product", description = "제품 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 제품 등록 메서드
    @Operation(
            summary = "제품등록",
            description = "JWT 인증필요, 기업 제품 등록 가능, 사용자 불가",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest request,
                                           @AuthenticationPrincipal CompanyDetails companyDetails) {
        Long comanyId = companyDetails.getCompanyId();
        productService.createProduct(comanyId, request);
        return ResponseEntity.ok().body(Map.of("message", "제품이 등록되었습니다."));
    }

    // 제품 수정 메서드
    @Operation(
            summary = "제품수정",
            description = "JWT 인증필요, 기업 제품 수정 가능, 사용자 불가",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductRequest request,
                                           @PathVariable Long productId,
                                           @AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        productService.updateProduct(productId, request, companyId);
        return ResponseEntity.ok(Map.of("message", "제품이 수정되었습니다."));
    }

    // 제품 삭제
    @Operation(
            summary = "제품삭제",
            description = "JWT 인증필요, 기업 제품 삭제 가능, 사용자 불가",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId,
                                           @AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        productService.deleteProduct(productId, companyId);
        return ResponseEntity.ok(Map.of("message", "제품이 삭제되었습니다."));
    }

    // 제품 목록 조회
    @Operation(
            summary = "제품목록조회",
            description = "JWT 인증필요, 기업 제품 목록 조회 가능, 사용자 조회 가능(홈페이지)",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("/list")
    public ResponseEntity<List<ProductListResponse>> getProductsByCompany(@AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        List<ProductListResponse> products = productService.getProductsByCompany(companyId);
        return ResponseEntity.ok(products);
    }

    // 제품 상세 조회
    @Operation(
            summary = "제품상세조회",
            description = "JWT 인증필요, 기업 제품 상세 조회 가능, 사용자 조회 가능(홈페이지)    ",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("/list/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponse response = productService.getProductDetail(productId);
        return ResponseEntity.ok(response);
    }



}
