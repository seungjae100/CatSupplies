package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailResponse {

    private String name;
    private int price;
    private String imgUrl;
    private String description;
    private String companyName;
    private int stockQuantity;

    public static ProductDetailResponse fromEntity(Product product) {
        return ProductDetailResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .description(product.getDescription())
                .companyName(product.getCompany().getCompanyName())
                .stockQuantity(product.getStock().getQuantity())
                .build();
    }
}
