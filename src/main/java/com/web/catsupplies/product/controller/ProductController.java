package com.web.catsupplies.product.controller;

import com.web.catsupplies.product.application.CreateProductRequest;
import com.web.catsupplies.product.application.ProductService;
import com.web.catsupplies.product.application.UpdateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 제품 등록 메서드
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest request,
                              @RequestParam("companyId") Long comanyId) {
        productService.createProduct(comanyId, request);
        return ResponseEntity.ok().body(Map.of("message", "제품이 등록되었습니다."));
    }

    // 제품 수정 메서드
    @PostMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductRequest request,
                                           @PathVariable Long productId) {
        productService.updateProduct(productId, request);
        return ResponseEntity.ok(Map.of("message", "제품이 수정되었습니다."));
    }


}
