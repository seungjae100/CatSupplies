package com.web.catsupplies.company.domain;

import com.web.catsupplies.common.constant.RegexPatterns;
import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import com.web.catsupplies.user.domain.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "companies")
public class Company extends BaseTimeEntity {

    // 기본키
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 핸드폰번호
    @Column(nullable = false)
    private String phone;

    // 주소
    @Column(nullable = false)
    private String address;

    // 회사이름
    @Column(nullable = false)
    private String companyName;

    // 대표
    @Column(nullable = false)
    private String boss;

    // 사업자등록번호
    @Column(nullable = false, unique = true)
    private String licenseNumber;

    // 권한
    @Enumerated(EnumType.STRING)
    private Role role = Role.COMPANY;

    // 회사와 제품의 연관매핑
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> product = new ArrayList<>();

    @Builder
    public Company(String email, String password, String phone, String address, String companyName, String boss, String licenseNumber) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.companyName = companyName;
        this.boss = boss;
        this.licenseNumber = licenseNumber;
        this.product = new ArrayList<>();
    }

    // Product: 연관관계 편의 메서드 추가
    public void addProduct(Product product) {
        this.product.add(product);
        if (product.getCompany() != this) {
            product.setCompany(this);
        }
    }

    // 정보 수정에 필요한 메서드
    public void changePassword(String password) {
        if (!password.matches(RegexPatterns.PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("비밀번호 형식이 유효하지 않습니다.");
        }
        this.password = password;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changeCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void changeBoss(String Boss) {
        this.boss = boss;
    }

    public void changelicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    // 제품 삭제
    public void removeProduct(Product product) {
        this.product.remove(product);
        if (product.getCompany() == this) {
            product.setCompany(null); // 관계 해제
        }
    }
}
