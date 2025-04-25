package com.web.catsupplies.user;

import com.web.catsupplies.common.exception.AccessDeniedException;
import com.web.catsupplies.common.exception.NotFoundException;
import com.web.catsupplies.user.application.*;
import com.web.catsupplies.user.domain.User;
import com.web.catsupplies.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServicefailTest {

    @InjectMocks
    private UserService userSerivce;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;


    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void register_fail_duplicateEmail() {
        RegisterRequest request = new RegisterRequest("test@gmail.com", "Password1234*", "홍홍홍", "01012341234", "서울");

        when(userRepository.existsByEmailAndDeletedFalse(request.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userSerivce.register(request);
        });
    }

    @Test
    @DisplayName("로그인 실패 - 이메일이 없음")
    void login_fail_emailNotFound() {
        LoginRequest request = new LoginRequest("NotEmail@Gmail.com", "Passwrod2*");
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(userRepository.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userSerivce.login(request, response);
        });
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_wrongPassword() {
        LoginRequest request = new LoginRequest("test@gmail.com", "WrongPassword");
        HttpServletResponse response = mock(HttpServletResponse.class);

        User user = User.create("test@gmail.com", "홍길동", "encodedPassword", "01012341234", "서울");

        when(userRepository.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            userSerivce.login(request, response);
        });
    }

    @Test
    @DisplayName("정보 수정 실패 - 본인이 아님")
    void modify_fail_unauthorized() {

        ModifyRequest request = new ModifyRequest("newPassword1234*", "지은이", "01022224444", "부산");

        Long userId = 1L;
        Long loginUserId = 2L;

        assertThrows(AccessDeniedException.class, () -> {
            userSerivce.modify(request, userId, loginUserId);
        });

    }

    @Test
    @DisplayName("정보 수정 실패 - 유저가 데이터베이스에 없음")
    void modify_fail_userNotFound() {
        ModifyRequest request = new ModifyRequest("newPasswre23*", "김지수", "01044442222", "울릉도");

        Long userId = 1L;
        Long loginUserId = 1L;

        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userSerivce.modify(request, userId, loginUserId);
        });
    }

    @Test
    @DisplayName("탈퇴 실패 - 유저가 데이터베이스에 없음")
    void deleteUser_fail_notFound() {
        Long userId = 1L;

        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userSerivce.deleteUser(userId);
        });
    }

    @Test
    @DisplayName("탈퇴 실패 - 이미 탈퇴한 유저인 경우")
    void deleteUser_fail_alreadyDeleted() {
        Long userId = 1L;
        User user = User.create("test@gmail.com", "홍길동", "Password1234*", "01012341234", "서울");

        user.remove(); // deleted = true 처리 이미 탈퇴한
        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userSerivce.deleteUser(userId);
        });
    }

    // -------
    // 토큰 관련 실패

    @Test
    @DisplayName("AccessToken 재발급 실패 - 쿠키 없음")
    void refreshToken_fail_cookieMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // 쿠키 자체가 없음
        when(request.getCookies()).thenReturn(null);

        // 우리가 기대하는 동작은 tokenService.reAccessToken 호출 자체가 안되거나, 내부에서 오류 발생
        assertThrows(AccessDeniedException.class, () -> {
            userSerivce.reAccessToken(request, response);

        });
    }

    @Test
    @DisplayName("AccessToken 재발급 실패 - RefreshToken 없음")
    void reAccessToken_fail_refreshTokenMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Cookie cookie = new Cookie("accessToken", "expired-token");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        doThrow(new AccessDeniedException("RefreshToken이 존재하지 않습니다."))
                .when(tokenService).reAccessToken(response, "expired-token");

        assertThrows(AccessDeniedException.class, () -> {
            userSerivce.reAccessToken(request, response);
        });
    }

    @Test
    @DisplayName("로그인 실패 - RefreshToken Redis 저장 실패")
    void login_fail_redisSaveException() {
        LoginRequest request = new LoginRequest("test@gmail.com", "Password1234*");
        HttpServletResponse response = mock(HttpServletResponse.class);

        User user = User.create("test@gmail.com", "홍길동", "encodedPwd", "01012345678", "서울");
        when(userRepository.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenService.createAccessToken(anyString(), any())).thenReturn("access-token");
        when(tokenService.createRefreshToken(anyString(), any())).thenReturn("refresh-token");

        doThrow(new RuntimeException("Redis 오류발생")).when(tokenService)
                .RedisSaveRefreshToken(eq(user.getEmail()), eq("refresh-token"));

        assertThrows(RuntimeException.class, () -> {
            userSerivce.login(request, response);
        });
    }


}
