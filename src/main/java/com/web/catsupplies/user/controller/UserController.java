package com.web.catsupplies.user.controller;

import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.user.application.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

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
        String accessToken = tokenService.createAccessToken(loginResponse.getEmail());
        String refreshToken = tokenService.createRefreshToken(loginResponse.getEmail());

        // RefreshToken 을 Redis 에 저장
        tokenService.RedisSaveRefreshToken(loginResponse.getEmail(), refreshToken);
        // AccessToken HttpOnly 쿠키에 저장
        CookieUtils.setCookie(response, "accessToken", accessToken, 60 * 60); // 1 시간

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
        userService.logout(request, response);
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/re_accessToken")
    public ResponseEntity<Map<String, String>> reAccessToken(HttpServletResponse response, HttpServletRequest request) {
        // AccessToken 을 재발급할 이메일을 가져옴
        String accessToken = WebUtils.getCookie(request, "accessToken") != null ?
                WebUtils.getCookie(request, "accessToken").getValue() : null;

        // 만약 AccessToken 이 null 이거나 유효하지 않은 AccessToken 인 경우,
        // AccessToken 이 없다는 메세지를 바디에 보낸다.
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "AccessToken이 없습니다."));
        }

        String email = jwtTokenProvider.getEmail(accessToken);
        tokenService.re_accessToken(response, email);

        return ResponseEntity.ok(Map.of("message", "AccessToken이 재발급되었습니다."));
    }


}
