package com.web.catsupplies.company.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyLoginResponse {
    // Swagger API Test를 위한 로그인 반환용
    private String accessToken;
}
