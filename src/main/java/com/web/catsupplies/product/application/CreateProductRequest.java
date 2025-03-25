package com.web.catsupplies.product.application;

import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.product.domain.Stock;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateProductRequest {

    @NotBlank(message = "제품코드를 입력해주세요.")
    private String code;

    @NotBlank(message = "제품이름를 입력해주세요.")
    private String name;

    @NotBlank(message = "제품가격을 입력해주세요.")
    @Min(value = 100)
    private int price;

    @NotBlank(message = "제품이미지를 넣어주세요.")
    private String imgUrl;

    @NotBlank(message = "제품설명을 입력해주세요.")
    private String description;

    @Min(value = 0, message = "재고 수량은 0 이상이여야 합니다.")
    private Integer stockQuantity;

    // DTO -> Entity 변환
    public Product toEntity(Company company, Stock stock) {
        return Product.builder()
                .code(this.code)
                .name(this.name)
                .price(this.price)
                .imgUrl(this.imgUrl)
                .description(this.description)
                .stock(stock)
                .company(company)
                .build();
    }
}
