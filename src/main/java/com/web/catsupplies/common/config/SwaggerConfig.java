package com.web.catsupplies.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "냥이와 API 명세서", version = "v1", description = "JWT 기반 인증 시스템")
)
@SecurityScheme(
        name = "jwtAuth",                 // 위 security 와 일치해야함
        type = SecuritySchemeType.HTTP,   // HTTP 형식
        scheme = "bearer",                // Bearer 방식
        bearerFormat = "JWT"              // 형식 명시
)
public class SwaggerConfig {
}
