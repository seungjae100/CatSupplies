package com.web.catsupplies.product.controller;

import com.web.catsupplies.common.exception.AccessDeniedException;
import com.web.catsupplies.common.jwt.CompanyDetails;
import com.web.catsupplies.product.application.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 제품 목록 조회 ( 기업 )
    @Operation(
            summary = "제품목록조회(기업)",
            description = "JWT 인증필요, 기업 제품 목록 조회 가능, 사용자 조회 가능(홈페이지)",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping("/list")
    public ResponseEntity<List<ProductListResponse>> getProductsByCompany(@AuthenticationPrincipal CompanyDetails companyDetails) {

        if (companyDetails == null) {
            throw new AccessDeniedException("기업 로그인이 필요합니다.");
        }

        Long companyId = companyDetails.getCompanyId();
        List<ProductListResponse> products = productService.getProductsByCompany(companyId);
        return ResponseEntity.ok(products);
    }

    // 제품목록 전체 조회 ( 로그인, 비로그인 )
    @Operation(
            summary = "제품목록 전체 조회",
            description = "JWT 없이 사용가능"
    )
    @GetMapping("/all/list")
    public ResponseEntity<List<ProductListResponse>> getAllProducts() {
        List<ProductListResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 제품 상세 조회 (사용자)
    @Operation(
            summary = "제품상세조회(사용자)",
            description = "JWT 인증필요, 기업 제품 상세 조회 가능, 사용자 조회 가능(홈페이지)    ",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("/list/user/{productId}")
    public ResponseEntity<ProductDetailForUserResponse> getProductDetailForUser(@PathVariable Long productId) {
        ProductDetailForUserResponse response = productService.getProductDetailForUser(productId);
        return ResponseEntity.ok(response);
    }

    // 제품 상세 조회 (기업)
    @Operation(
            summary = "제품상세조회(기업)",
            description = "JWT 인증필요, 기업 제품 상세 조회 가능, 사용자 조회 가능(홈페이지)    ",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PreAuthorize("hasRole('COMPANY')")
    @GetMapping("/list/company/{productId}")
    public ResponseEntity<ProductDetailForCompanyResponse> getProductDetailForCompany(@PathVariable Long productId,
                                                                                      @AuthenticationPrincipal CompanyDetails companyDetails) {
        if (companyDetails == null) {
            throw new AccessDeniedException("기업 계정이 필요합니다.");
        }

        Long companyId = companyDetails.getCompanyId();

        ProductDetailForCompanyResponse response = productService.getProductDetailForCompany(productId, companyId);
        return ResponseEntity.ok(response);
    }



}
