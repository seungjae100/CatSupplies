package com.web.catsupplies.product.repository;

import com.web.catsupplies.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // (회사) 내가 등록한 회사 제품 조회 (회사 기본키)
    List<Product> findAllByCompanyId(Long companyId);
}
