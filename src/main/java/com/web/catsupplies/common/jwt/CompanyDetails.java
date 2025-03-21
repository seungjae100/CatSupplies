package com.web.catsupplies.common.jwt;

import com.web.catsupplies.company.domain.Company;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CompanyDetails implements UserDetails {

    private final Company company;

    public CompanyDetails(Company company) {
        this.company =company;
    }

    public Company getCompany() {
        return this.company;
    }

    public Long getCompanyId() {
        return company.getId();
    }

    public String getComanyName() {
        return company.getCompanyName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_COMPANY"));
    }

    @Override
    public String getPassword() {
        return company.getPassword(); // 암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return company.getEmail(); // 인증 기준은 이메일
    }

    // 기타 override true 반환

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
