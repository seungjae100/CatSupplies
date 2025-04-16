package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailForUserResponse {
    // 유저가 조회하는 제품 상세 조회
    private String name;
    private int price;
    private String imgUrl;
    private String description;
    private String companyName;

    public static ProductDetailForUserResponse fromEntity(Product product) {
        return ProductDetailForUserResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .description(product.getDescription())
                .companyName(product.getCompany().getCompanyName())
                .build();
    }
}
