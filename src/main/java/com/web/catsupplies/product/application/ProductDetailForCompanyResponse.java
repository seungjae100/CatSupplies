package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailForCompanyResponse {
    // 기업이 조회하는 제품 상세
    private String name;
    private int price;
    private String imgUrl;
    private String description;
    private String companyName;
    private int stockQuantity;

    public static ProductDetailForCompanyResponse fromEntity(Product product) {
        return ProductDetailForCompanyResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .description(product.getDescription())
                .companyName(product.getCompany().getCompanyName())
                .stockQuantity(product.getStock().getQuantity())
                .build();
    }
}
