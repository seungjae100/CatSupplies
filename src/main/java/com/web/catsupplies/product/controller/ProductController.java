package com.web.catsupplies.product.controller;

import com.web.catsupplies.product.application.CreateProductRequest;
import com.web.catsupplies.product.application.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @RequestMapping("/create")
    public ResponseEntity<?> (@ResponseBody @Valid
    CreateProductRequest request, Long company)
}
