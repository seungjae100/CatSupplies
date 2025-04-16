package com.web.catsupplies.company.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyModifyRequest {

    private String password;

    private String phone;

    private String address;

    private String companyName;

    private String boss;

    private String licenseNumber;
}
