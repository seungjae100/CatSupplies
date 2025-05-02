package com.web.catsupplies.common.config;

import com.web.catsupplies.common.jwt.CompanyDetailsSerevice;
import com.web.catsupplies.common.jwt.CustomUserDetailsService;
import com.web.catsupplies.common.jwt.JwtAuthenticationFilter;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService; // 직접 만든 UserDetailsService 생성자 주입
    private final CompanyDetailsSerevice companyDetailsSerevice;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // csrf 비활성화
                .authorizeHttpRequests(auth -> auth
                        //Swagger 접근 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // 회원가입, 로그인 허용
                        .requestMatchers("/api/user/register",  "/api/user/login",
                                        "/api/company/register", "/api/company/login").permitAll()

                        // 재발급 권한 허용
                        .requestMatchers("/api/company/reAccessToken", "/api/user/reAccessToken").permitAll()

                        // Company만 허용
                        .requestMatchers("/api/company/**").hasRole("COMPANY")

                        // User만 허용
                        .requestMatchers("/api/user/**").hasRole("USER")

                        // 제품 조회는 모두 가능 (비로그인도 가능)
                        .requestMatchers("/api/products/all/list").permitAll()

                        // 제품 상세 조회는 구분
                        .requestMatchers("/api/products/user/**").hasRole("USER")
                        .requestMatchers("/api/products/company/**").hasRole("COMPANY")

                        // 제품 등록, 수정, 삭제는 기업만
                        .requestMatchers("/api/products/create", "/api/products/update/**", "/api/products/delete").hasRole("COMPANY")

                        // 주문은 사용자만
                        .requestMatchers("/api/orders/**").hasRole("USER")

                        // 결제도 사용자만
                        .requestMatchers("/api/payments/**").hasRole("USER")

                        // 재고 조작은 기업만 가능
                        .requestMatchers("/api/stocks/**").hasRole("COMPANY")

                        // 재고 변경 이력도 기업만 가능
                        .requestMatchers("/api/stocks/history/**").hasRole("COMPANY")

                        // 그 외는 인증필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, companyDetailsSerevice), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
