package com.web.catsupplies.product.domain;

import com.web.catsupplies.company.domain.Company;
import com.web.catsupplies.stock.domain.Stock;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product")
@Entity
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

    // 제품 삭제 메서드
    @Column(nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company; // 기업과의 조인관계 (기업 1 : N 제품)

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock; // 재고와의 조인관계 (재고 1 : 1 제품)

    // Stock: 연관관계 편의 메서드 추가
    public void setStock(Stock stock) {
        this.stock = stock;
        // 양방향 연관관계 설정
        if (stock.getProduct() != this) {
            stock.setProduct(this);
        }
    }

    // Company: 연관관계 편의 메서드 추가
    public void setCompany(Company company) {
        this.company = company;
    }

    // 제품 생성 메서드
    public static Product create(String code, String name, int price, String imgUrl, String description, Company company, Stock stock) {
        Product product = new Product();
        product.code = code;
        product.name = name;
        product.price = price;
        product.imgUrl = imgUrl;
        product.description = description;
        product.company = company;

        // 연관관계 편의 메서드
        company.addProduct(product); // Company ↔ Product
        product.setStock(stock);     // Product ↔ Stock (양방향 연결)

        return product;
    }

    // product update 메서드 추가
    public void update(String code, String name, Integer price, String imgUrl, String description) {
        if (code != null) this.code = code;
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (imgUrl != null) this.imgUrl = imgUrl;
        if (description != null) this.description = description;
    }

    public void remove() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

}
