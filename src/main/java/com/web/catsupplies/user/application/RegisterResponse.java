package com.web.catsupplies.user.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterResponse {

    private Long id;
    private String email;
    private String name;

}
