package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {

    private String name;
    private int price;
    private String imgUrl;
    private String description;
    private String companyName;
    private int stockQuantity;

    public static ProductDetailResponse fromEntity(Product product) {
        return new ProductDetailResponse(
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                product.getDescription(),
                product.getCompany().getCompanyName(),
                product.getStock().getQuantity()
        );
    }
}
