package com.web.catsupplies.product.application;

import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.company.repository.CompanyRepository;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.product.domain.Stock;
import com.web.catsupplies.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    public Long createProduct(Long companyId, CreateProductRequest request) {
        // 로그인한 기업의 정보를 가져온다.
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        // 재고 기본 수량은 0 으로 생성
        Stock stock = Stock.builder()
                .quantity(0)
                .build();

        // DTO -> 엔티티 변환 후 데이터베이스에 저장
        Product product = request.toEntity(company, stock);

        // 제품 등록 저장
        Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }
}
