package com.web.catsupplies.user.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    // Swagger API Test를 위한 로그인 반환용
    private String accessToken;
}
