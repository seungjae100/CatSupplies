package com.web.catsupplies.product.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateProductRequest {

    @NotBlank(message = "제품코드를 입력해주세요.")
    @Schema(description = "제품의 코드를 입력합니다.", example = "PRO-002")
    private String code;

    @NotBlank(message = "제품이름를 입력해주세요.")
    @Schema(description = "제품의 이름을 입력합니다.", example = "고양이사료굿")
    private String name;


    @Min(value = 100, message = "제품가격을 입력해주세요.")
    @Schema(description = "제품의 가격을 입력합니다.", example = "12,000원")
    private int price;

    @NotBlank(message = "제품이미지를 넣어주세요.")
    @Schema(description = "제품의 이미지URL을 입력합니다.", example = "https:imgfewagjealo213152")
    private String imgUrl;

    @NotBlank(message = "제품설명을 입력해주세요.")
    @Schema(description = "제품의 설명을 입력합니다.", example = "가장 베스트 셀러로 뽑힌 인기 상품입니다.")
    private String description;

    @Min(value = 0, message = "재고 수량은 0 이상이여야 합니다.")
    @Schema(description = "입고수량을 입력합니다.", example = "100 개")
    private Integer stockQuantity;
}
