package com.web.catsupplies.serivce;

import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.user.application.*;
import com.web.catsupplies.user.domain.Role;
import com.web.catsupplies.user.domain.User;
import com.web.catsupplies.user.repository.RefreshTokenRepository;
import com.web.catsupplies.user.repository.UserRepository;
import io.micrometer.core.annotation.TimedSet;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // JUnit 테스트에서 Mockito 를 사용할 때 필요한 확장 기능을 활성화하는 역할을 함
                                    // Mock 객체를 활용하여 Spring Context 를 로드하지 않고 빠르게 단위 테스트틀 수행할 수 있도록 도와줌
                                    // @Mock, @InjectMocks 와 함께 사용하여 서비스 레이어의 단위 테스트를 효율적으로 수행
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private Validator validator;
    private User testUser;

    @BeforeEach
    void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        testUser = User.builder()
                .email("test@gmail.com")
                .password("encodedPassword")
                .name("테스트")
                .phone("010-1234-1234")
                .address("경기도 양주시")
                .role(Role.USER)
                .build();

        // 필요 없는 Stubbing 예외 방지
        lenient().when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        lenient().when(userRepository.existsByEmail(any())).thenReturn(false);

        lenient().when(userRepository.findByEmail(any())).thenReturn(Optional.of(testUser));
        lenient().when(passwordEncoder.matches(any(), any())).thenReturn(true);
        lenient().when(jwtTokenProvider.createAccessToken(any())).thenReturn("accessToken");
        lenient().when(jwtTokenProvider.createRefreshToken(any())).thenReturn("refreshToken");
    }

    // 회원가입 성공 테스트
    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_성공_테스트() {
        // given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@gmail.com")
                .password("password1234")
                .name("테스트")
                .phone("010-1234-1234")
                .address("경기도 양주시")
                .build();

        when(userRepository.save(any())).thenReturn(
                User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(Role.USER)
                .build());

        // when
        RegisterResponse response = userService.register(request);

        // then
        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
    }

    // 중복 이메일로 인한 회원가입 실패
    @Test
    @DisplayName("중복 이메일 회원가입 실패")
    void 중복_이메일_회원가입_실패() {
        // given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@gmail.com")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterRequests")
    @DisplayName("회원가입_필수값_누락_테스트")
    void 필수값_누락_회원가입_실패_테스트(RegisterRequest request) {
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "필요한 필드가 누락 된 경우 유효성 검사가 실패합니다");
    }
    static Stream<RegisterRequest> invalidRegisterRequests() {
        return Stream.of(
                RegisterRequest.builder().email(null).build(),
                RegisterRequest.builder().password(null).build(),
                RegisterRequest.builder().name(null).build(),
                RegisterRequest.builder().phone(null).build(),
                RegisterRequest.builder().address(null).build()
        );
    }

    // 로그인 실패 (데이터 베이스에 존재하지 않는 이메일)
    @Test
    @DisplayName("로그인 실패 - 데이터베이스에 존재하지 않는 이메일")
    void 로그인_실패_존재하지_않는_이메일() {
        // given
        LoginRequest request = new LoginRequest("nonononn@gmail.com", "password1234");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.login(request));
    }

    // 로그인 실패 (비밀번호 틀림)
    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void 로그인_실패_비밀번호_틀림() {
        // given
        LoginRequest request = new LoginRequest("test@gmail.com", "WhatThePassword");

        when(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> userService.login(request));
    }

    // 로그인 성공 테스트
    @Test
    @DisplayName("로그인 성공 - 올바른 정보 입력")
    void 로그인_성공() {
        // given
        LoginRequest request = new LoginRequest("test@gmail.com", "password1234");

        when(passwordEncoder.matches(request.getPassword(), testUser.getPassword())).thenReturn(true);

        // when
        LoginResponse response = userService.login(request);

        // then
        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("테스트", response.getName());
        assertEquals(Role.USER, response.getRole());

    }

    // AccessToken 생성 확인
    @Test
    @DisplayName("AccessToken 생성 확인")
    void AccessToken_생성_확인() {
        // given
        String email = "test@gmail.com";
        when(jwtTokenProvider.createAccessToken(email)).thenReturn("accessToken");

        // when
        String token = userService.generateAccessToken(email);

        // then
        assertNotNull(token);
        assertEquals("accessToken", token);
    }

    // RefreshToken 생성 확인
    @Test
    @DisplayName("RefreshToken 생성 확인")
    void RefreshToken_생성_확인() {
        // given
        String email = "test@gmail.com";
        when(jwtTokenProvider.createRefreshToken(email)).thenReturn("refreshToken");

        // when
        String token = userService.generateRefreshToken(email);

        // then
        assertNotNull(token);
        assertEquals("refreshToken", token);
    }

    // Redis 에 RefreshToken 이 저장되는지 확인
    @Test
    @DisplayName("Redis에 토큰 저장 확인")
    void Redis_RefreshToken_저장확인() {
        // given
        String email = "test@gmail.com";
        String refreshToken = "refreshToken";

        // when
        userService.RedisRefreshToken(email, refreshToken);

        // then
        verify(refreshTokenRepository, times(1)).save(any());
    }

    // 로그아웃 시 RefreshToken 삭제 테스트
    @Test
    @DisplayName("로그아웃 - RefreshToken 삭제")
    void 로그아웃_RefreshToken_삭제() {
        // given
        String email = "test@gmail.com";

        // when
        userService.removeRefreshToken(email);

        // then
        verify(refreshTokenRepository, times(1)).deleteById(email);

    }

}
