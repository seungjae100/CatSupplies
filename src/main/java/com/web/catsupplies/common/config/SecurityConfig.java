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
                        // 공개 API
                        .requestMatchers("/api/user/register",  "/api/user/login",
                                        "/api/company/register", "/api/company/login").permitAll()

                        // 권한 제어
                        .requestMatchers("/api/products/", "/api/products/**").hasAnyRole("USER", "COMPANY")
                        .requestMatchers("/api/products/create", "/api/products/update/**", "/api/products/delete").hasRole("COMPANY")

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
