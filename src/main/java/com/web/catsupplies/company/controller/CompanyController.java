package com.web.catsupplies.company.controller;

import com.web.catsupplies.common.exception.AccessDeniedException;
import com.web.catsupplies.common.jwt.CompanyDetails;
import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.company.application.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Company", description = "기업 관련 API")
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 기업 회원가입
    @Operation(
            summary = "회원가입",
            description = "JWT 없이 사용가능"
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CompanyRegisterRequest request) {
        companyService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    // 로그인
    @Operation(
            summary = "로그인",
            description = "JWT 없이 사용가능"
    )
    @PostMapping("/login")
    public ResponseEntity<CompanyLoginResponse> login(@RequestBody @Valid CompanyLoginRequest request, HttpServletResponse response) {
        CompanyLoginResponse companyLoginResponse = companyService.login(request, response);
        return ResponseEntity.ok(companyLoginResponse);
    }

    // 정보 수정
    @Operation(
            summary = "정보수정",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PatchMapping("/{companyId}")
    public ResponseEntity<String> modify(@Valid @RequestBody CompanyModifyRequest request, @AuthenticationPrincipal CompanyDetails companyDetails, @PathVariable Long companyId) {
        companyService.modify(request, companyId, companyDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }

    // 정보 조회
    @Operation(
            summary = "정보조회",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @GetMapping("/profile")
    public ResponseEntity<CompanyResponse> getCompany(@AuthenticationPrincipal CompanyDetails companyDetails) {
        Long companyId = companyDetails.getCompanyId();
        CompanyResponse response = companyService.getCompany(companyId);
        return ResponseEntity.ok(response);
    }

    // 재발급토큰
    @Operation(
            summary = "AccessToken 재발급",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping("/reAccessToken")
    public ResponseEntity<String> reAccessToken(HttpServletRequest request,
                                                HttpServletResponse response,
                                                @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String expiredAccessToken = null;

        // Swagger에서 Authorization 헤더로 토큰을 넘겼을 경우 처리
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            expiredAccessToken = authHeader.substring(7);
        } else {
            // 쿠키에서 AccessToken을 꺼내는 기존의 로직
            expiredAccessToken = CookieUtils.getCookie(request, "accessToken")
                    .orElseThrow(() -> new AccessDeniedException("AccessToken이 존재하지 않습니다."));
        }

        companyService.reAccessToken(request, response);
        return ResponseEntity.ok("AccessToken이 재발급되었습니다.");
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

    // 탈퇴
    @Operation(
            summary = "회원탈퇴",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCompany(@AuthenticationPrincipal CompanyDetails companyDetails) {
        companyService.deleteCompany(companyDetails.getCompanyId());
        return ResponseEntity.noContent().build();
    }
}
