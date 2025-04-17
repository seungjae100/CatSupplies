package com.web.catsupplies.company.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyLoginRequest {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    @Schema(description = "이메일 입력란", example = "example@test.com")
    private String email;

    @Schema(description = "비밀번호", example = "8~16 자, 영문,숫자,특수문자 필수")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

}
