package com.web.catsupplies.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.catsupplies.common.config.SecurityTestConfig;
import com.web.catsupplies.user.application.LoginRequest;
import com.web.catsupplies.user.application.UserService;
import com.web.catsupplies.user.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_성공_테스트() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("test@gmail.com", "Password1234*");

        // userService.login (Void 반환)
        doNothing().when(userService).login(any(), any());

        // when & then
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("로그인이 완료되었습니다."));
    }
}
