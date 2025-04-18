package com.web.catsupplies.user.application;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "핸드폰번호", example = "핸드폰번호 - 없이 작성")
    private String phone;

    @Schema(description = "주소를 입력합니다.", example = "경기도 00시 00~")
    private String address;
}
