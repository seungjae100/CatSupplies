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

    @Transactional
    public String reAccessToken(HttpServletResponse response, HttpServletRequest request) {
        // AccessToken 쿠키에서 가져오기
        String accessToken = CookieUtils.getCookie(request, "accessToken");

        // AccessToken 이 없거나 유효성이 상실한 경우는 예외를 발생시킨다
        if (accessToken == null || jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomUnauthorizedException("유효하지 않은 AccessToken입니다. 재발급해주세요.");
        }
        // AccessToken 에 있는 이메일 사용자 정보를 가져온다.
        String email = jwtTokenProvider.getEmail(accessToken);
        // 이메일 정보에서 저장된 RefreshToken 이 있는지 확인한다.
        String storedRefreshToken = getStoredRefreshToken(email);

        if (storedRefreshToken == null || !validateRefreshToken(storedRefreshToken)) {
            throw new CustomUnauthorizedException("RefreshToken이 유효하지 않습니다. 다시 로그인해주세요");
        }
        // 새로운 AccessToken 발급
        String newAccessToken = createAccessToken(email);
        // AccessToken 을 HttpOnly에 저장
        CookieUtils.setCookie(response, "accessToken", newAccessToken, 60 * 60);

        return "AccessToken이 재발급되었습니다.";
    }

    // RefreshToken 검증
    public boolean validateRefreshToken(String refreshToken) {
        return jwtTokenProvider.validateToken(refreshToken);
    }

    // 저장된 RefreshToken 가져오기
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

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            String email = jwtTokenProvider.getEmail(accessToken);

            // RefreshToken 토큰 삭제
            refreshTokenRepository.deleteById(email);

            // AccessToken 쿠키 삭제
            CookieUtils.deleteCookie(response, "accessToken");
        }
    }
}
