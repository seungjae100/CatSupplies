package com.web.catsupplies.product.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null값은 무시된다.
public class UpdateProductRequest {

    @Schema(description = "제품의 코드를 입력합니다.", example = "TEST-001")
    private String code;

    @Schema(description = "제품의 이름을 입력합니다..", example = "고양이사료굿")
    private String name;

    @Schema(description = "제품의 가격을 입력합니다.", example = "12000")
    @Min(value = 100)
    private Integer price; // null을 허용하기 위한 Integer

    @Schema(description = "제품의 이미지를 입력합니다.", example = "https://imgtest")
    private String imgUrl;

    @Schema(description = "제품의 설명을 입력합니다.", example = "가장 베스트 셀러로 뽑힌 인기 상품입니다.")
    private String description;

    @Schema(description = "제품의 재고수량을 입력합니다.", example = "89")
    @Min(0)
    private Integer stockQuantity; // 재고 수량만 따로 받기
}
