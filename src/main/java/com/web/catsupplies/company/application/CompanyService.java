package com.web.catsupplies.company.application;

import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.company.repository.CompanyRepository;
import com.web.catsupplies.user.application.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    // 회원가입
    public void register(CompanyRegisterRequest request) {
        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Company company = Company.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .companyName(request.getCompanyName())
                .boss(request.getBoss())
                .licenseNumber(request.getLicenseNumber())
                .build();

        companyRepository.save(company);
    }

    // 로그인
    public void login(CompanyLoginRequest request, HttpServletResponse response) {
        Company company = companyRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(company.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(company.getEmail());

        tokenService.RedisSaveRefreshToken(company.getEmail(), refreshToken);

        CookieUtils.setCookie(response, "accessToken", accessToken, 60 * 60);

    }

    // AccessToken 재발급
    public void reAccessToken(HttpServletResponse response, String email) {
         tokenService.reAccessToken(response, email);
    }

    // 로그아웃
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        tokenService.logout(request, response);
    }



}
