package com.web.catsupplies.company.controller;

import com.web.catsupplies.company.application.CompanyLoginRequest;
import com.web.catsupplies.company.application.CompanyRegisterRequest;
import com.web.catsupplies.company.application.CompanyService;
import com.web.catsupplies.user.application.TokenService;
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

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid CompanyRegisterRequest request) {
        companyService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid CompanyLoginRequest request, HttpServletResponse response) {
        companyService.login(request, response);
        return ResponseEntity.ok("로그인이 완료되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        companyService.logout(response, request);
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    @PostMapping("/reAccessToken")
    public ResponseEntity<String> reAccessToken(HttpServletRequest request, HttpServletResponse response) {
        tokenService.reAccessToken(response, request);
        return ResponseEntity.ok(companyService.reAccessToken(request, response));
    }
}
