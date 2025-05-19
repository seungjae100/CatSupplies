package com.web.catsupplies.user.application;

import com.web.catsupplies.common.constant.RegexPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRequest {

    @Schema(description = "비밀번호", example = "8~16 자, 영문,숫자,특수문자 필수")
    private String password;

    @Schema(description = "이름을 입력합니다.", example = "홍길동")
    private String name;

    @Schema(description = "핸드폰번호", example = "010-1234-1234")
    private String phone;

    @Schema(description = "주소를 입력합니다.", example = "경기도")
    private String address;
}
