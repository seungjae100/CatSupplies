package com.web.catsupplies.user.application;

import com.web.catsupplies.common.constant.RegexPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    @Schema(description = "이메일을 입력합니다.", example = "test@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = RegexPatterns.PASSWORD_PATTERN, message = "비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Schema(description = "비밀번호", example = "8~16 자, 영문,숫자,특수문자 필수")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Schema(description = "이름을 입력합니다.", example = "홍길동")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Schema(description = "핸드폰번호", example = "핸드폰번호 - 없이 작성")
    private String phone;

    @NotBlank(message = "주소를 입력해주세요.")
    @Schema(description = "주소를 입력합니다.", example = "경기도 00시 00~")
    private String address;
}
