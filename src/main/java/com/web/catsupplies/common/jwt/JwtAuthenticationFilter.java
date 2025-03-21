package com.web.catsupplies.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService; // 사용자 정보를 가져오는 서비스
    private final CompanyDetailsSerevice companyDetailsSerevice;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // HttpOnly 쿠키에서 accessToken 가져오기
        Cookie cookie = WebUtils.getCookie(request, "accessToken");

        if (cookie != null) {
            String token = cookie.getValue();

            // JWT 유효성 검사
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmail(token);
                String role = jwtTokenProvider.getRole(token);

                // 이미 인증이 되어있는지 확인 (중복 방지)
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // DB 에서 사용자 정보 조회
                    UserDetails userDetails;
                    if ("ROLE_COMPANY".equals(role)) {
                        userDetails = companyDetailsSerevice.loadUserByUsername(email);
                    } else {
                        userDetails = userDetailsService.loadUserByUsername(email);
                    }

                    // 인증 객체 생성 및 설정
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext 에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }
        chain.doFilter(request, response);
    }
}
