package com.web.catsupplies.product.application;

import com.web.catsupplies.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductListResponse {
    // 제품 리스트
    @Schema(description = "Swagger 테스트의 편의성을 높이기 위한 id 노출")
    private Long productId;

    @Schema(description = "제품의 이름을 조회합니다.", example = "고양이사료굿")
    private String name;

    @Schema(description = "제품의 가격을 조회합니다.", example = "12,000원")
    private int price;

    @Schema(description = "제품의 이미지를 조회합니다.", example = "https:imgfewagjealo213152")
    private String imgUrl;

    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .build();
    }
}
