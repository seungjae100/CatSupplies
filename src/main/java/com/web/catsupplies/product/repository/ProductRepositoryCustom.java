package com.web.catsupplies.product.repository;

import com.web.catsupplies.product.domain.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    // QueryDSL 적용을 위한 Repository

    // 탈퇴한 회사의 제품을 제외하고 제품목록 조회
    List<Product> findAllAvailableProducts();
}
