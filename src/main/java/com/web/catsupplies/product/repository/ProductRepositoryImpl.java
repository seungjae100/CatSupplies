package com.web.catsupplies.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.web.catsupplies.company.domain.QCompany;
import com.web.catsupplies.product.domain.QProduct;
import com.web.catsupplies.product.domain.Product;

import org.springframework.stereotype.Repository;
import jakarta.persistence.*;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Product> findAllAvailableProducts() {
        QProduct product = QProduct.product;
        QCompany company = QCompany.company;

        return queryFactory
                .selectFrom(product)
                .join(product.company, company)
                .where(
                        product.deleted.isFalse(),
                        company.deleted.isFalse()
                )
                .fetch();
    }
}
