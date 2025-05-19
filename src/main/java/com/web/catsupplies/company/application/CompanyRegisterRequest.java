package com.web.catsupplies.company.application;

import com.web.catsupplies.common.constant.RegexPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegisterRequest {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    @Schema(description = "이메일 입력란", example = "company@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = RegexPatterns.PASSWORD_PATTERN, message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    @Schema(description = "비밀번호", example = "8~16 자, 영문,숫자,특수문자 필수")
    private String password;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Schema(description = "핸드폰번호", example = "010-1234-1234")
    private String phone;

    @NotBlank(message = "주소를 입력해주세요.")
    @Schema(description = "주소", example = "주소 작성")
    private String address;

    @NotBlank(message = "회사이름을 입력해주세요.")
    @Schema(description = "회사이름", example = "회사 이름을 작성합니다.")
    private String companyName;

    @NotBlank(message = "대표명을 입력해주세요.")
    @Schema(description = "대표자", example = "대표자 이름을 작성합니다.")
    private String boss;

    @NotBlank(message = "사업자등록번호를 입력해주세요")
    @Schema(description = "사업자등록번호", example = "사업자등록번호 작성")
    private String licenseNumber;
}
