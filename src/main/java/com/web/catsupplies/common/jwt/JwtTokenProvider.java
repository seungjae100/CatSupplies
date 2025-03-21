package com.web.catsupplies.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // JWT 비밀키 값 주입
    @Value("${jwt.secretKey}")
    private String secretKey;
    // AccessToken 유효기간
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    // RefreshToken 유효기간
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    // JWT 서명을 위한 SecretKey (HMAC SHA 알고리즘에 사용)
    private Key key;

    // 객체가 생성된 후, secretKey 기반으로 HMAC SHA 키를 설정하는 초기화 메서드
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // AccessToken email 기반으로 생성
    public String createAccessToken(String email, String role) {
        return createToken(email, accessTokenExpiration, role);
    }
    // RefreshToken email 기반으로 생성
    public String createRefreshToken(String email, String role) {
        return createToken(email, refreshTokenExpiration, role);
    }

    // JWT 토큰 생성 로직
    private String createToken(String email, long expirationTime, String role) {
        return Jwts.builder()
                .setSubject(email) // 토큰의 subject (사용자의 email 저장)
                .claim("role", role)
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // HMAC SHA256 알고리즘을 사용하여 서명
                .compact(); // 토큰을 문자열로 변환하여 반환
    }

    // JWT 토큰의 유효성을 검증하는 메서드
    public boolean validateToken(String token) {
        // 서명 키를 이용하여 토큰의 유효성을 검사 (만료 여부 포함)
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.error("손상된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("토큰이 비어있거나 유효하지 않습니다.");
        }
        return false;
    }

    // JWT 토큰에서 email(사용자 식별 정보)을 추출하는 메서드
    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject(); // subject 에 저장된 email 값 반환
    }

    // 만료된 AccessToken에서도 email을 추출할 수 있도록 처리 (재발급시 필요)
    public String getEmailFromExpiredToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // 정상적인 토큰이면 email 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject(); // 만료된 토큰에서도 email 반환 가능
        } catch (Exception e) {
            return null; // 토큰이 손상되었거나 변조된 경우 null 반환
        }
    }
    // 만료된 AccessToken에서도 role을 추출할 수 있도록 처리 (재발급시 필요)
    public String getRoleFromExpiredToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // 토큰에서 role 정보를 가져온다.
    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
