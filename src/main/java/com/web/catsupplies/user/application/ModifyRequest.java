package com.web.catsupplies.user.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRequest {

    private String password;

    private String name;

    private String phone;

    private String address;
}
