package com.web.catsupplies.user.controller;

import com.web.catsupplies.common.exception.AccessDeniedException;
import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.user.application.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @Operation(
            summary = "회원가입",
            description = "JWT 없이 사용가능",
            security = @SecurityRequirement(name = "") // JWT 인증 제외
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다."); // 201 반환
    }

    // 로그인
    @Operation(
            summary = "로그인",
            description = "JWT 없이 사용가능",
            security = @SecurityRequirement(name = "") // JWT 인증 제외
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = userService.login(request, response); // 사용자 정보 가져오기
        return ResponseEntity.ok(loginResponse);
    }

    // 정보 수정
    @Operation(
            summary = "정보수정",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PatchMapping("/{userId}")
    public ResponseEntity<String> modify(@RequestBody @Valid ModifyRequest request, @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long userId) {
        userService.modify(request, userId, userDetails.getUserId());
        return ResponseEntity.ok().build();
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
            // 쿠키에서 accessToken을 꺼내는 기존 로직
            expiredAccessToken = CookieUtils.getCookie(request, "accessToken")
                    .orElseThrow(() -> new AccessDeniedException("AccessToken이 존재하지 않습니다."));
        }
        userService.reAccessToken(request, response);
        return ResponseEntity.ok("AccessToken이 재발급되었습니다.");
    }

    // 로그아웃
    @Operation(
            summary = "로그아웃",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, HttpServletRequest request) {
        userService.logout(request, response);
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }

    // 탈퇴
    @Operation(
            summary = "회원탈퇴",
            description = "JWT",
            security = @SecurityRequirement(name = "jwtAuth") // JWT 인증
    )
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }


}
