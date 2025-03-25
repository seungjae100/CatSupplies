package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductListResponse {

    private String name;
    private int price;
    private String imgUrl;

    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .build();
    }
}
