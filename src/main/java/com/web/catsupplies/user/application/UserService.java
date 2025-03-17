package com.web.catsupplies.user.application;

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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    // 회원가입
    public RegisterResponse register(@Valid RegisterRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // Builder 를 통한 데이터 요청입력
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(Role.USER)
                .build();

        // 요청 DB에 저장
        userRepository.save(user);

        // 요청 완료에 따른 응답 반환
        return RegisterResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    // 로그인
    public LoginResponse login(@Valid LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));
        // 비밀번호 검증 ( 클라이언트에서 요청이 온 비밀번호와 유저의 DB 에서 가져온 비밀번호를 비교했는데 다르다면)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        return LoginResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    // 로그아웃
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // AccessToken 에서 이메일 추출 (삼항연산자)
        String accessToken = WebUtils.getCookie(request, "accessToken") != null ?
                WebUtils.getCookie(request, "accessToken").getValue() : null;

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            String email = jwtTokenProvider.getEmail(accessToken);

            // RefreshToken 삭제 (Redis 에서 삭제 )
            tokenService.removeRefreshToken(email, response);
        }
    }


}
