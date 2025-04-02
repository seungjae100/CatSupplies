package com.web.catsupplies.company.controller;

import com.web.catsupplies.company.application.CompanyLoginRequest;
import com.web.catsupplies.company.application.CompanyRegisterRequest;
import com.web.catsupplies.company.application.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Company", description = "기업 관련 API")
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 기업 회원가입
    @Operation(
            summary = "회원가입",
            description = "JWT 없이 사용가능",
            security = @SecurityRequirement(name = "") // JWT 인증 제외
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CompanyRegisterRequest request) {
        companyService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인
    @Operation(
            summary = "로그인",
            description = "JWT 없이 사용가능",
            security = @SecurityRequirement(name = "") // JWT 인증 제외
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid CompanyLoginRequest request, HttpServletResponse response) {
        companyService.login(request, response);
        return ResponseEntity.ok("로그인이 완료되었습니다.");
    }

    // 로그아웃
    @Operation(
            summary = "로그아웃",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        companyService.logout(response, request);
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    // 재발급토큰
    @Operation(
            summary = "AccessToken 재발급",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping("/reAccessToken")
    public ResponseEntity<String> reAccessToken(HttpServletRequest request, HttpServletResponse response) {
        companyService.reAccessToken(request, response);
        return ResponseEntity.ok("AccessToken이 재발급되었습니다.");
    }
}
