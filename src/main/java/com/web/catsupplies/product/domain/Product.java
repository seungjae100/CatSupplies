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


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column(nullable = false, unique = true)
    private String code; // 제품코드

    @Column(nullable = false)
    private String name; // 제픔이름

    @Column(nullable = false)
    private int price; // 제품가격

    @Column(nullable = false)
    private String imgUrl; // 제품 이미지

    @Column(nullable = false)
    private String description; // 제품 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company; // 기업과의 조인관계 (기업 1 : N 제품)

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock; // 재고와의 조인관계 (재고 1 : 1 제품)

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
    public void update(String code, String name, Integer price, String imgUrl, String description) {
        if (code != null) this.code = code;
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (imgUrl != null) this.imgUrl = imgUrl;
        if (description != null) this.description = description;
    }


}
