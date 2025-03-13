package com.web.catsupplies.user.controller;

import com.web.catsupplies.user.application.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 반환
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = userService.login(request); // 사용자 정보 가져오기

        // JWT 발급
        String accessToken = userService.generateAccessToken(loginResponse.getEmail());
        String refreshToken = userService.generateRefreshToken(loginResponse.getEmail());

        // RefreshToken 을 Redis 에 저장
        userService.RedisRefreshToken(loginResponse.getEmail(), refreshToken);

        // AccessToken 을 HttpOnly 쿠키에 저장
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok("로그아웃 완료");
    }

}
