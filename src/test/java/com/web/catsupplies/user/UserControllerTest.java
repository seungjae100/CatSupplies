package com.web.catsupplies.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.catsupplies.common.config.SecurityTestConfig;
import com.web.catsupplies.common.jwt.CustomUserDetails;
import com.web.catsupplies.user.application.*;
import com.web.catsupplies.user.controller.UserController;
import com.web.catsupplies.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityTestConfig.class) // csrf에 대한 테스트 분리
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails userDetails;



    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_성공_테스트() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("test@gmail.com", "Password1234*");

        LoginResponse loginResponse = new LoginResponse("accessToken");

        when(userService.login(any(LoginRequest.class), any(HttpServletResponse.class)))
                .thenReturn(loginResponse);

        // when & then
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loginResponse)));
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_성공_테스트() throws Exception {
        RegisterRequest request = new RegisterRequest("test@gmail.com", "Password1234*","지은이", "01012341234", "경기도");
        doNothing().when(userService).register(any());

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("회원가입이 완료되었습니다."));
    }

    @BeforeEach
    void setUp() {
        // 1. 테스트용 User 객체 생성
        User user = User.create("test@gmail.com", "홍길동", "Password1234*", "01012341234", "서울시");

        // 2. Id 세팅 (reflection 사용)
        ReflectionTestUtils.setField(user, "id", 1L);

        // 3. CustomUserDetails
        userDetails = new CustomUserDetails(user);

        // 4. 인증 정보 설정 (Authentication + SecurityContext)
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }



    @Test
    @DisplayName("로그아웃 성공 테스트")
    void 로그아웃_성공_테스트() throws Exception {
        doNothing().when(userService).logout(any(HttpServletRequest.class), any(HttpServletResponse.class));

        mockMvc.perform(post("/api/user/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("로그아웃이 완료되었습니다."));

    }

    @Test
    @DisplayName("정보 수정 성공 테스트")
    void 정보_수정_성공_테스트() throws Exception {
        // given
        ModifyRequest request = new ModifyRequest("Password1234%", "홍홍홍", "01099990000", "서울");

        doNothing().when(userService).modify(any(), eq(1L), eq(1L));

        // when & then
        mockMvc.perform(patch("/api/user/1")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void 회원탈퇴_성공_테스트() throws Exception {
        doNothing().when(userService).deleteUser(eq(1L));

        mockMvc.perform(delete("/api/user/delete")
                        .with(user(userDetails)))
                .andExpect(status().isNoContent());
    }
}
