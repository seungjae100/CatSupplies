package com.web.catsupplies.user.application;

import com.web.catsupplies.common.exception.AccessDeniedException;
import com.web.catsupplies.common.exception.NotFoundException;
import com.web.catsupplies.common.jwt.CookieUtils;
import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.user.domain.RefreshToken;
import com.web.catsupplies.user.domain.Role;
import com.web.catsupplies.user.domain.User;
import com.web.catsupplies.user.repository.RefreshTokenRepository;
import com.web.catsupplies.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.WebUtils;

@Validated
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    // 회원가입
    public void register(RegisterRequest request) {

        // 이메일 중복 확인
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // Builder 를 통한 데이터 요청입력
        User user = User.create(
                request.getEmail(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhone(),
                request.getAddress()
        );

        // 요청 DB에 저장
        userRepository.save(user);
    }

    // 로그인
    public void login(LoginRequest request, HttpServletResponse response) {

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));
        // 비밀번호 검증 ( 클라이언트에서 요청이 온 비밀번호와 유저의 DB 에서 가져온 비밀번호를 비교했는데 다르다면)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        // JWT 발급
        String accessToken = tokenService.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = tokenService.createRefreshToken(user.getEmail(), user.getRole());

        // RefreshToken 을 Redis 에 저장
        tokenService.RedisSaveRefreshToken(user.getEmail(), refreshToken);
        // AccessToken HttpOnly 쿠키에 저장
        CookieUtils.setCookie(response, "accessToken", accessToken, 60 * 60); // 1 시간

    }

    // 정보 수정
    public void modify(ModifyRequest request, Long userId, Long loginUserId) {
        // 로그인한 본인인지 확인
        if (!userId.equals(loginUserId)) {
            throw new AccessDeniedException("로그인한 사람만이 수정할 수 있습니다.");
        }

        // 데이터베이스에서 유저 조회하기
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        // 정보 부분 수정 필드
        if (request.getName() != null) {
            user.changeName(request.getName());
        }

        if (request.getPassword() != null) {
            user.changePassword(request.getPassword());
        }

        if (request.getPhone() != null) {
            user.changePhone((request.getPhone()));
        }

        if (request.getAddress() != null) {
            user.changeAddress(request.getAddress());
        }
    }

    // AccessToken 재발급
    public void reAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String expiredToken = CookieUtils.getCookie(request, "accessToken");
        tokenService.reAccessToken(response, expiredToken);
    }

    // 로그아웃
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        tokenService.logout(request, response);
    }

    // 사용자 탈퇴
    public void deleteUser(Long userId) {

        // 유저 정보가 데이터베이스에 저장되어 있는지 확인
        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));

        // 이미 유저가 탈퇴한 상황인지 확인
        if (user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 유저입니다.");
        }

        user.remove();
    }
}
