package com.web.catsupplies.company.application;

import com.web.catsupplies.common.exception.AccessDeniedException;
import com.web.catsupplies.common.exception.NotFoundException;
import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.company.repository.CompanyRepository;
import com.web.catsupplies.user.application.TokenService;
import com.web.catsupplies.user.domain.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    // 회원가입
    @Transactional
    public void register(CompanyRegisterRequest request) {
        if (companyRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Company company = Company.create(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone(),
                request.getAddress(),
                request.getCompanyName(),
                request.getBoss(),
                request.getLicenseNumber()
        );

        companyRepository.save(company);
    }

    // 로그인
    public void login(CompanyLoginRequest request, HttpServletResponse response) {

        Company company = companyRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일입니다."));
        // 비밀번호 검증 ( 클라이언트에서 요청이 온 비밀번호와 유저의 DB 에서 가져온 비밀번호를 비교했는데 다르다면)
        if (!passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // JWT 발급
        String accessToken = jwtTokenProvider.createAccessToken(company.getEmail(), Role.COMPANY.name());
        String refreshToken = jwtTokenProvider.createRefreshToken(company.getEmail(), Role.COMPANY.name());

        // RefreshToken 을 Redis 에 저장
        tokenService.RedisSaveRefreshToken(company.getEmail(), refreshToken);
        // AccessToken HttpOnly 쿠키에 저장
        CookieUtils.setCookie(response, "accessToken", accessToken, 60 * 60);

    }

    // 정보 수정
    @Transactional
    public void modify(CompanyModifyRequest request, Long companyId, Long loginCompanyId) {
        // 로그인한 본인 기업인지 확인
        if (!companyId.equals(loginCompanyId)) {
            throw new AccessDeniedException("로그인한 기업만이 수정할 수 있습니다.");
        }
        // 데이터베이스에 기록이 있는 기업정보인지 확인
        Company company = companyRepository.findByIdAndDeletedFalse(companyId)
                .orElseThrow(() -> new NotFoundException("없는 기업입니다."));

        // 정보 부분 수정
        if (request.getPassword() != null) {
            company.changePassword(request.getPassword());
        }

        if (request.getPhone() != null) {
            company.changePhone(request.getPhone());
        }

        if (request.getAddress() != null) {
            company.changeAddress(request.getAddress());
        }

        if (request.getCompanyName() != null) {
            company.changeCompanyName(request.getCompanyName());
        }

        if (request.getBoss() != null) {
            company.changeBoss(request.getBoss());
        }

        if (request.getLicenseNumber() != null) {
            company.changelicenseNumber(request.getLicenseNumber());
        }
    }

    // AccessToken 재발급
    public void reAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String expiredToken = CookieUtils.getCookie(request, "accessToken")
                .orElseThrow(() -> new AccessDeniedException("AccessToken이 존재하지 않습니다."));
        tokenService.reAccessToken(response, expiredToken);
    }

    // 로그아웃
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        tokenService.logout(request, response);
    }

    // 기업 탈퇴
    @Transactional
    public void deleteCompany(Long CompanyId) {

        // 기업 정보가 데이터베이스에 저장되어 있는지 확인
        Company company = companyRepository.findByIdAndDeletedFalse(CompanyId)
                .orElseThrow(() -> new NotFoundException("기업정보가 존재하지 않습니다."));

        // 이미 기업이 탈퇴한 상황
        if (company.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 기업입니다.");
        }

        company.remove();
    }
}
