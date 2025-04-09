package com.web.catsupplies.product.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null값은 무시된다.
public class UpdateProductRequest {

    private String code;

    private String name;

    @Min(value = 100)
    private Integer price; // null을 허용하기 위한 Integer

    private String imgUrl;

    private String description;

    @Min(0)
    private Integer stockQuantity; // 재고 수량만 따로 받기
}
