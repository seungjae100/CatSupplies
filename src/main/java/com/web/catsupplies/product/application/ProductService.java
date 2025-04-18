package com.web.catsupplies.product.application;

import com.web.catsupplies.common.exception.NotFoundException;
import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.company.repository.CompanyRepository;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.stock.domain.Stock;
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

    // 제품 등록
    @Transactional
    public Long createProduct(Long companyId, CreateProductRequest request) {
        // 로그인한 기업의 정보를 가져온다.
        Company company = companyRepository.findByIdAndDeletedFalse(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        int quantity = 0;
        Stock stock = Stock.create(quantity);
        if (request.getStockQuantity() != null && request.getStockQuantity() > 0) {
            stock.inboundStock(request.getStockQuantity());
        }


        Product product = Product.create(
                request.getCode(),
                request.getName(),
                request.getPrice(),
                request.getImgUrl(),
                request.getDescription(),
                company,
                stock
        );
        return productRepository.save(product).getId();
    }

    // 제품 수정
    @Transactional
    public void updateProduct(Long productId, UpdateProductRequest request, Long companyId) {
        // 데이터베이스에 제품이 존재하는지 확인 (Id) 기준
        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
        // 기업 로그인으로 본인인지 확인
        if (!product.getCompany().getId().equals(companyId)) {
            throw new SecurityException("해당 제품을 수정할 권한이 없습니다.");
        }
        // 수정 시 제품코드 중복확인
        if (request.getCode() != null && !request.getCode().equals(product.getCode())) {
            if (productRepository.existsByCodeAndDeletedFalse(request.getCode())) {
                throw new IllegalArgumentException("이미 존재하는 제품코드입니다.");
            }
        }

        product.update(
                request.getCode(),
                request.getName(),
                request.getPrice(),
                request.getImgUrl(),
                request.getDescription()
        );

        // 재고수량을 입력하지 않을 시 기존의 재고수량으로 진행
        if (request.getStockQuantity() != null) {
            product.getStock().updateQuantity(request.getStockQuantity());
        }
    }

    // 제품 삭제
    @Transactional
    public void deleteProduct(Long productId, Long companyId) {
        // 제품이 존재하는지 확인
        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다."));
        // 해당 제품을 등록한 본인 회사인지 비교하여 확인
        if (!product.getCompany().getId().equals(companyId)) {
            throw new SecurityException("해당 제품을 삭제할 권한이 없습니다.");
        }

        product.remove();
    }

    // 내가 등록한 제품 조회
    @Transactional(readOnly = true)
    public List<ProductListResponse> getProductsByCompany(Long companyId) {
        List<Product> products = productRepository.findAllByCompanyIdAndDeletedFalse(companyId);

        return products.stream()
                .map(ProductListResponse::from)
                .collect(Collectors.toList());
    }

    // 등록한 제품 상세조회 ( 사용자 )
    @Transactional(readOnly = true)
    public ProductDetailForUserResponse getProductDetailForUser(Long productId) {
        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("해당 제품이 존재하지 않습니다."));
        return ProductDetailForUserResponse.fromEntity(product);
    }

    // 등록한 제품 상세조회 ( 기업 )
    @Transactional(readOnly = true)
    public ProductDetailForCompanyResponse getProductDetailForCompany(Long productId, Long companyId) {
        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("해당 제품이 존재하지 않습니다."));

        if (!product.getCompany().getId().equals(companyId)) {
            throw new SecurityException("본인의 제품만 조회할 수 있습니다.");
        }

        return ProductDetailForCompanyResponse.fromEntity(product);
    }
}