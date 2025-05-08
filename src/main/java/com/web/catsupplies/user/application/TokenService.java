package com.web.catsupplies.user.application;

import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.user.domain.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    // AccessToken 생성 ( 쿠키용 )
    public String createAccessToken(String email, Role role) {
        return jwtTokenProvider.createAccessToken(email, "ROLE_" + role.name());
    }

    // RefreshToken 생성 ( Redis )
    public String createRefreshToken(String email, Role role) {
        return jwtTokenProvider.createRefreshToken(email, "ROLE_" +role.name());
    }

    // RefreshToken 을 Redis 에 저장  (TTL 적용)
    @Transactional
    public void RedisSaveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
                "RT:" + email,
                refreshToken,
                Duration.ofMillis(refreshTokenExpiration)
        );
    }

    // 재발급
    @Transactional
    public void reAccessToken(HttpServletResponse response, String expiredAccessToken) {
        // 1. 토큰에서 이메일과 역할 꺼내기
        Claims claims = jwtTokenProvider.getClaimsFromToken(expiredAccessToken);

        // 2. null 체크
        if (claims == null) {
            throw new IllegalArgumentException("토큰 정보가 유효하지 않습니다. 다시 로그인해주세요.");
        }
        // 3. Claims에서 email과 role 꺼내기
        String email = claims.getSubject(); // subjectsms email로 저장됨
        String roleString = claims.get("role", String.class);

        if (email == null || roleString == null) {
            throw new IllegalArgumentException("토큰 정보가 누락되었습니다. 다시 로그인해주세요.");
        }

        // 저장된 RefreshToken 가져오기
        String storedRefreshToken = getStoredRefreshToken(email);

        // RefreshToken 이 없거나 유효하지 않으면 예외 발생
        if (storedRefreshToken == null || !validateRefreshToken(storedRefreshToken)) {
            throw new IllegalArgumentException("RefreshToken 이 유효하지 않습니다. 다시 로그인해주세요.");
        }

        // 기존 만료된 AccessToken 삭제
        CookieUtils.deleteCookie(response, "accessToken");

        // 새로운 AccessToken 발급
        Role role = Role.valueOf(roleString.replace("ROLE_", ""));
        String newAccessToken = createAccessToken(email, role);

        // 새 AccessToken 을 HttpOnly 쿠키에 저장
        CookieUtils.setCookie(response, "accessToken", newAccessToken, 60 * 60);
    }

    // RefreshToken 검증
    public boolean validateRefreshToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    // 이메일에서 저장된 RefreshToken 가져오기
    public String getStoredRefreshToken(String email) {
        return redisTemplate.opsForValue().get("RT:" + email);
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.delete("RT:" + email);
    }

    // RefreshToken 삭제 ( 로그아웃 )
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // AccessToken 쿠키에서 가져오기
        Optional<String> optionalAccessToken = CookieUtils.getCookie(request, "accessToken");

        // AccessToken 이 없으면 이미 로그아웃된 상태 → 정상 종료
        if (optionalAccessToken.isEmpty()) {
            return; // 예외 발생 X, 그냥 정상 종료
        }

        String accessToken = optionalAccessToken.get();

        // Claims 추출
        Claims claims = jwtTokenProvider.getClaimsFromToken(accessToken);

        if (claims == null) {
            return; // 손상된 토큰이거나 유효하지 않음
        }

        String email = claims.getSubject(); // getEmail

        // RefreshToken 조회 및 검증
        String refreshToken = getStoredRefreshToken(email);
        if (refreshToken != null && validateRefreshToken(refreshToken)) {
            deleteRefreshToken(email);
        }
        // AccessToken 쿠키 삭제
        CookieUtils.deleteCookie(response, "accessToken");
    }
}
