package com.web.catsupplies.common.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal; // 사용자의 이메일 또는 UserDetails 객체
    private String credentials; // JWT 토큰 자체를 저장

    // 인증되지 않은 상태의 JWT 토큰 ( SecurityContextHolder 에 인증 정보가 없는 경우 )
    public JwtAuthenticationToken(String token) {
        super(null);
        this.credentials = token;
        this.principal = null;
        setAuthenticated(false); // 인증되지 않은 상태로 설정
    }

    // 인증된 상태의 JWT 토큰 (SecurityContextHolder에 저장할 객체)
    public JwtAuthenticationToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal; // 사용자 정보  (email 또는 UserDetails)
        this.credentials = credentials;
        setAuthenticated(true); // 인증 상태로 설정
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
