package com.web.catsupplies.user.application;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyRequest {

    private String password;

    private String name;

    private String phone;

    private String address;
}
