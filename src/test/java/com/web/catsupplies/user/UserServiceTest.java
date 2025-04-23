package com.web.catsupplies.user;

import com.web.catsupplies.user.application.*;
import com.web.catsupplies.user.domain.Role;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userSerivce;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    // 회원가입 성공
    @Test
    @DisplayName("회원가입 성공 테스트")
    void register_success() {
        RegisterRequest request = new RegisterRequest(
                "test@gmail.com", "Password1234*", "홍길동", "01012341234", "서울시"
        );

        // 이메일 중복 아님
        when(userRepository.existsByEmailAndDeletedFalse(request.getEmail())).thenReturn(false);

        // 회원 저장
        when(userRepository.save(any(User.class)))
                .thenAnswer(new Answer<User>() {
                    @Override
                    public User answer(InvocationOnMock invocation) {
                        return invocation.getArgument(0);
                    }
                });

        userSerivce.register(request);

        verify(userRepository).save(any(User.class));
    }

    // 로그인 테스트
    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() {
        LoginRequest request = new LoginRequest("test@gmail.com", "Password1234*");
        HttpServletResponse response = mock(HttpServletResponse.class);

        User user = User.create("test@gmail.com", "홍길동", "Password1234*", "01012341234", "서울시");
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByEmailAndDeletedFalse(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenService.createAccessToken(anyString(), any(Role.class))).thenReturn("accessToken");
        when(tokenService.createRefreshToken(anyString(), any(Role.class))).thenReturn("refreshToken");

        userSerivce.login(request, response);

        verify(tokenService).RedisSaveRefreshToken(eq(user.getEmail()), eq("refreshToken"));
        verify(tokenService).createAccessToken(anyString(), any(Role.class));
    }

    // 정보 수정 테스트
    @Test
    @DisplayName("정보 수정 테스트")
    void modify_success() {
        Long userId = 1L;
        Long loginUserId = 1L;
        ModifyRequest request = new ModifyRequest("Password0000*", "분홍꽃", "01044442222", "경기도");

        User user = User.create("test@gmail.com", "홍길동", "Password1234*", "01012341234", "서울시");
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.of(user));

        userSerivce.modify(request, userId, loginUserId);

        assertEquals("분홍꽃", user.getName());
        assertEquals("Password0000*", user.getPassword());
        assertEquals("01044442222", user.getPhone());
        assertEquals("경기도", user.getAddress());

    }

    // 재발급 테스트
    @Test
    @DisplayName("AccessToken 재발급")
    void reAccessToken_success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("accessToken", "expired-token")});

        userSerivce.reAccessToken(request, response);

        verify(tokenService).reAccessToken(response, "expired-token");
    }

    // 로그아웃 테스트
    @Test
    @DisplayName("로그아웃 테스트")
    void logout_success() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        userSerivce.logout(request, response);

        verify(tokenService).logout(request, response);
    }

    // 회원탈퇴 테스트
    @Test
    @DisplayName("회원탈퇴 테스트")
    void deleteUser_success() {
        Long userId = 1L;
        User user = User.create("test@gmail.com", "홍길동", "Password1234*", "01012341234", "서울시");
        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findByIdAndDeletedFalse(userId)).thenReturn(Optional.of(user));

        userSerivce.deleteUser(userId);

        assertTrue(user.isDeleted());
    }
}
