package com.web.catsupplies.user.application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRequest {

    @Schema(description = "비밀번호를 입력합니다.", example = "password1234*")
    private String password;

    @Schema(description = "이름을 입력합니다.", example = "홍길동")
    private String name;

    @Schema(description = "전화번호를 입력합니다.", example = "01012341234")
    private String phone;

    @Schema(description = "주소를 입력합니다.", example = "경기도 00시 00~")
    private String address;
}
