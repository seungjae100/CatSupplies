package com.web.catsupplies.user.application;

import com.web.catsupplies.user.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private String email;
    private String name;
    private Role role;
}
