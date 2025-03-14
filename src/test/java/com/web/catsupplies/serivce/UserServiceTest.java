package com.web.catsupplies.serivce;

import com.web.catsupplies.common.jwt.JwtTokenProvider;
import com.web.catsupplies.user.application.RegisterRequest;
import com.web.catsupplies.user.application.RegisterResponse;
import com.web.catsupplies.user.application.UserService;
import com.web.catsupplies.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class) // JUnit 테스트에서 Mockito 를 사용할 때 필요한 확장 기능을 활성화하는 역할을 함
                                    // Mock 객체를 활용하여 Spring Context 를 로드하지 않고 빠르게 단위 테스트틀 수행할 수 있도록 도와줌
                                    // @Mock, @InjectMocks 와 함께 사용하여 서비스 레이어의 단위 테스트를 효율적으로 수행
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        userRepository.deleteAll(); // 각 테스트 실행 전에 데이터 초기화
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

        // when
        RegisterResponse response = userService.register(request);

        // then
        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
    }


}
