package com.web.catsupplies.product.application;

import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.company.repository.CompanyRepository;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.product.domain.Stock;
import com.web.catsupplies.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public Long createProduct(Long companyId, CreateProductRequest request) {
        // 로그인한 기업의 정보를 가져온다.
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        // 재고 수량 null 이면 0으로 표시
        int quantity = request.getStockQuantity() != null ? request.getStockQuantity() : 0;

        Stock stock = Stock.builder()
                .quantity(quantity)
                .build();

        // DTO -> 엔티티 변환 후 데이터베이스에 저장
        Product product = request.toEntity(company, stock);

        // 제품 등록 저장
        Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    // 제품 수정
    @Transactional
    public void updateProduct(Long productId, UpdateProductRequest request, Long companyId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));

        if (!product.getCompany().getId().equals(companyId)) {
            throw new SecurityException("해당 제품을 수정할 권한이 없습니다.");
        }

        product.update(
                request.getName(),
                request.getPrice(),
                request.getImgUrl(),
                request.getDescription()
        );

        if (request.getStockQuantity() != null) {
            product.getStock().updateQuantity(request.getStockQuantity());
        }
    }

    // 제품 삭제
    @Transactional
    public void deleteProduct(Long productId, Long companyId) {
        // 1. 제품이 존재하는지 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
        // 2. 해당 제품을 등록한 본인 회사인지 비교하여 확인
        if (!product.getCompany().getId().equals(companyId)) {
            throw new SecurityException("해당 제품을 삭제할 권한이 없습니다.");
        }

        productRepository.delete(product);
    }

    // 내가 등록한 제품 조회
    @Transactional
    public List<ProductListResponse> getProductsByCompany(Long companyId) {
        List<Product> products = productRepository.findAllByCompanyId(companyId);

        return products.stream()
                .map(ProductListResponse::from)
                .collect(Collectors.toList());
    }
}