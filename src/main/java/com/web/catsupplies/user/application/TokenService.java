package com.web.catsupplies.user.application;

import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.common.exception.CustomUnauthorizedException;
import com.web.catsupplies.user.domain.RefreshToken;
import com.web.catsupplies.user.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // AccessToken 생성 ( 쿠키용 )
    public String createAccessToken(String email) {
        return jwtTokenProvider.createAccessToken(email);
    }

    // RefreshToken 생성 ( Redis )
    public String createRefreshToken(String email) {
        return jwtTokenProvider.createRefreshToken(email);
    }

    // RefreshToken 을 Redis 에 저장
    @Transactional
    public void RedisSaveRefreshToken(String email, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(email, refreshToken));
    }

    // 재발급
    @Transactional
    public void reAccessToken(HttpServletResponse response, String email) {
        // 저장된 RefreshToken 가져오기
        String storedRefreshToken = getStoredRefreshToken(email);

        // RefreshToken 이 없거나 유효하지 않으면 예외 발생
        if (storedRefreshToken == null || !validateRefreshToken(storedRefreshToken)) {
            throw new CustomUnauthorizedException("RefreshToken 이 유효하지 않습니다. 다시 로그인해주세요.");
        }

        // 기존 만료된 AccessToken 삭제
        CookieUtils.deleteCookie(response, "accessToken");

        // 새로운 AccessToken 발급
        String newAccessToken = createAccessToken(email);

        // 새 AccessToken 을 HttpOnly 쿠키에 저장
        CookieUtils.setCookie(response, "accessToken", newAccessToken, 60 * 60);
    }

    // RefreshToken 검증
    public boolean validateRefreshToken(String refreshToken) {
        return jwtTokenProvider.validateToken(refreshToken);
    }

    // 이메일에서 저장된 RefreshToken 가져오기
    public String getStoredRefreshToken(String email) {
        return refreshTokenRepository.findById(email)
                .map(RefreshToken::getRefreshToken)
                .orElse(null);
    }

    // RefreshToken 삭제 ( 로그아웃 )
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // AccessToken 쿠키에서 가져오기
        String accessToken = CookieUtils.getCookie(request, "accessToken");

        // AccessToken 이 없으면 이미 로그아웃된 상태 → 정상 종료
        if (accessToken == null) {
            return; // 예외 발생 X, 그냥 정상 종료
        }

        String email = null;

        // AccessToken 이 유효하면 email 추출
        if (jwtTokenProvider.validateToken(accessToken)) {
            jwtTokenProvider.getEmail(accessToken);
        } else {
            // AccessToken 이 만료된 경우 email 추출
            email = jwtTokenProvider.getEmailFromExpiredToken(accessToken);

            if (email != null) { // 이메일이 있으면 RefreshToken 확인
                String refreshToken = getStoredRefreshToken(email);
                if (refreshToken != null && validateRefreshToken(refreshToken)) {
                    reAccessToken(response, email); // AccessToken 재발급 후 로그아웃
                }
            }

            // email 이 없으면 RefreshToken 도 없거나 변조된 경우이므로 종료
            if (email == null) {
                return;
            }

            // RefreshToken 삭제 (로그아웃 처리)
            refreshTokenRepository.deleteById(email);

            // AccessToken 쿠키 삭제
            CookieUtils.deleteCookie(response, "accessToken");
        }
    }
}
