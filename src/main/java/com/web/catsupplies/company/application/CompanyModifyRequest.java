package com.web.catsupplies.company.application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyModifyRequest {

    @Schema(description = "비밀번호", example = "8~16 자, 영문,숫자,특수문자 필수")
    private String password;

    @Schema(description = "핸드폰번호", example = "010-1234-1234")
    private String phone;

    @Schema(description = "주소", example = "주소 작성")
    private String address;

    @Schema(description = "회사이름", example = "회사 이름을 작성합니다.")
    private String companyName;

    @Schema(description = "대표자", example = "대표자 이름을 작성합니다.")
    private String boss;

    @Schema(description = "사업자등록번호", example = "사업자등록번호 작성")
    private String licenseNumber;
}
