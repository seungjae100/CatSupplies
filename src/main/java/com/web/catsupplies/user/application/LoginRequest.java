package com.web.catsupplies.user.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    @Schema(description = "이메일을 입력합니다.", example = "user@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Schema(description = "비밀번호", example = "8~16 자, 영문,숫자,특수문자 필수")
    private String password;
}
