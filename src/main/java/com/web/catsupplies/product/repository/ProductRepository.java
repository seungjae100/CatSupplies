package com.web.catsupplies.product.repository;

import com.web.catsupplies.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 삭제된 제품 제외하고 제품의 기본키 조회
    Optional<Product> findByIdAndDeletedFalse(Long id);

    // 삭제된 제품 제외하고 기업이 제품 등록한 목록을 모두 조회
    List<Product> findAllByCompanyIdAndDeletedFalse(Long comanyId);

    // 삭제된 제품을 제외하고 중복된 코드가 있는지 조회
    boolean existsByCodeAndDeletedFalse(String code);

}
