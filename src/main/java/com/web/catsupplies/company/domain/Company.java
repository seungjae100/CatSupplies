package com.web.catsupplies.company.domain;

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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String boss;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private Role role = Role.COMPANY;

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

    // 제품 삭제
    public void removeProduct(Product product) {
        this.product.remove(product);
        if (product.getCompany() == this) {
            product.setCompany(null); // 관계 해제
        }
    }
}
