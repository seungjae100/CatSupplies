package com.web.catsupplies.company.application;

import com.web.catsupplies.common.constant.RegexPatterns;
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
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = RegexPatterns.PASSWORD_PATTERN, message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "회사이름을 입력해주세요.")
    private String companyName;

    @NotBlank(message = "대표명을 입력해주세요.")
    private String boss;

    @NotBlank(message = "사업자등록번호를 입력해주세요")
    private String licenseNumber;
}
