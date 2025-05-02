package com.web.catsupplies.company.application;

import com.web.catsupplies.company.domain.Company;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyResponse {
    // 회사 정보 조회를 위한 응답
    private Long companyId;
    private String email;
    private String phone;
    private String address;
    private String companyName;
    private String boss;
    private String licenseNumber;

    public static CompanyResponse from(Company company) {
        return CompanyResponse.builder()
                .companyId(company.getId())
                .email(company.getEmail())
                .phone(company.getPhone())
                .address(company.getAddress())
                .companyName(company.getCompanyName())
                .boss(company.getBoss())
                .licenseNumber(company.getLicenseNumber())
                .build();
    }


}
