package com.web.catsupplies.product.controller;

import com.web.catsupplies.common.jwt.CompanyDetails;
import com.web.catsupplies.product.application.CreateProductRequest;
import com.web.catsupplies.product.application.ProductListResponse;
import com.web.catsupplies.product.application.ProductService;
import com.web.catsupplies.product.application.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 제품 등록 메서드
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest request,
                                           @AuthenticationPrincipal CompanyDetails companyDetails) {
        Long comanyId = companyDetails.getCompanyId();
        productService.createProduct(comanyId, request);
        return ResponseEntity.ok().body(Map.of("message", "제품이 등록되었습니다."));
    }

    // 제품 수정 메서드
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductRequest request,
                                           @PathVariable Long productId,
                                           @AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        productService.updateProduct(productId, request, companyId);
        return ResponseEntity.ok(Map.of("message", "제품이 수정되었습니다."));
    }

    // 제품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId,
                                           @AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        productService.deleteProduct(productId, companyId);
        return ResponseEntity.ok(Map.of("message", "제품이 삭제되었습니다."));
    }

    // 제품 목록 조회
    @GetMapping("/List")
    public ResponseEntity<List<ProductListResponse>> getProductsByCompany(@AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        List<ProductListResponse> products = productService.getProductsByCompany(companyId);
        return ResponseEntity.ok(products);
    }



}
