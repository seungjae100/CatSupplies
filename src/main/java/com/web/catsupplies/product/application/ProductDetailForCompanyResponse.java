package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailForCompanyResponse {
    // 기업이 조회하는 제품 상세

    @Schema(description = "제품의 이름을 조회합니다.", example = "고양이사료굿")
    private String name;

    @Schema(description = "제품의 가격을 조회합니다.", example = "12,000원")
    private int price;

    @Schema(description = "제품의 이미지를 조회합니다.", example = "https:imgfewagjealo213152")
    private String imgUrl;

    @Schema(description = "제품의 설명을 조회합니다.", example = "가장 베스트 셀러로 뽑힌 인기 상품입니다.")
    private String description;

    @Schema(description = "제품의 회사명을 조회합니다.", example = "고냥이")
    private String companyName;

    @Schema(description = "제품의 재고를 조회합니다.", example = "99 개")
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
