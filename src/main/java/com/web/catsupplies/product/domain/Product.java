package com.web.catsupplies.product.domain;

import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;

    @Builder
    public Product(String code, String name, int price, String imgUrl, String description, Company company, Stock stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
        this.company = company;
        this.stock = stock;

        // Product 가 Stock 과 연결할 때, Stock 도 Product 를 설정하도록 함
        if (stock != null) {
            stock.setProduct(this);
        }
        // Product 가 Company 와 연결할 때 Company 도 Product 를 설정하도록 함
        if (company != null) {
            company.addProduct(this);
        }
    }

    // Stock: 연관관계 편의 메서드 추가
    public void setStock(Stock stock) {
        this.stock = stock;
        if (stock.getProduct() != this) {
            stock.setProduct(this);
        }
    }

    // Company: 연관관계 편의 메서드 추가
    public void setCompany(Company company) {
        this.company = company;
        if (!company.getProduct().contains(this)) {
            company.getProduct().add(this); // 양방향 관계 유지
        }
    }

    // product update 메서드 추가
    public void update(String name, int price, String imgUrl, String description) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
    }


}
