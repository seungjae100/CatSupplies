package com.web.catsupplies.user.application;

import com.web.catsupplies.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    // 사용자 정보 조회를 위한 응답 DTO
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String address;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }
}
