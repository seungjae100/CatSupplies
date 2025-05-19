package com.web.catsupplies.common.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final CompanyDetailsService companyDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // Swagger UI, Open API 관련 요청은 필터를 적용하지 않는다.
        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")
                || path.equals("/swagger-ui.html")
                || path.equals("/api/user/login")
                || path.equals("/api/user/register")
                || path.equals("/api/user/reAccessToken")
                || path.equals("/api/company/login")
                || path.equals("/api/company/register")
                || path.equals("/api/company/reAccessToken")
                || path.equals("/api/products/all/list")) {
            chain.doFilter(request, response);
            return;
        }

        // 토큰 추출 - 쿠키기반 (기본적인 인증의 흐름 - 배포 환경)
        String token = null;
        Cookie cookie = WebUtils.getCookie(request, "accessToken");
        if (cookie != null) {
            token = cookie.getValue();
        }

        // 토큰 추출 - Authorization 헤더 기반 (Swagger 테스트, Docker 환경 포함)
        if (token == null) {
            String bearer = request.getHeader("Authorization");
            System.out.println("Authorization Header: " + bearer);
            if (bearer != null && bearer.startsWith("Bearer ")) {
                token = bearer.substring(7); // "Bearer " 이후의 토큰 추출
            }
        }


        // JWT 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
                Claims claims = jwtTokenProvider.getClaimsFromToken(token);

        // 이미 인증이 되어있는지 확인 (중복 방지)
            if (claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                // DB 에서 사용자 정보 조회
                UserDetails userDetails = "ROLE_COMPANY".equals(role)
                    ? companyDetailsService.loadUserByUsername(email) : userDetailsService.loadUserByUsername(email);

                System.out.println("JWT ROLE: " + role);
                System.out.println("UserDetails: " + userDetails);
                System.out.println("Class of UserDetails: " + userDetails.getClass());
                System.out.println("BEFORE setAuth: " + SecurityContextHolder.getContext().getAuthentication());

                    // 인증 객체 생성 및 설정
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext 에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);


                }
            }

            chain.doFilter(request, response);
        }
    }