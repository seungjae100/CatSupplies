package com.web.catsupplies.stock.domain;

import com.web.catsupplies.product.domain.Product;
import com.web.catsupplies.stockhistory.domain.StockHistory;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Stock extends BaseTimeEntity {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column(nullable = false)
    private int quantity; // 수량

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 제품과의 1 : 1 조인

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockHistory> stockHistories = new ArrayList<>(); // 재고기록과의 1 : N 관계

    // Product: 연관관계 편의 메서드 추가
    public void setProduct(Product product) {
        this.product = product;
    }

    // StockHistory 연관관계 편의 메서드 추가
    public void addStockHistory(StockHistory stockHistory) {
        this.stockHistories.add(stockHistory);
            stockHistory.setStock(this); // StockHistory -> Stock 만 연결
    }
    // 재고 변경이력을 자동으로 저장
    private void updateStockHistory(StockStatus status, int quantityChange) {
        StockHistory history = StockHistory.createHistory(this, status, quantityChange);
        this.addStockHistory(history); // 양방향 연관관계 설정
    }


    // 재고 생성
    public static Stock create(int quantity) {
        Stock stock = new Stock();
        stock.quantity = quantity;
        return stock;
    }

    // 재고 수량 변경 메서드 (updateRequestDTO)
    public void updateQuantity(int newQuantity) {
        int change = newQuantity - this.quantity;

        if (change == 0) return; // 수량 변경이 없으면 종료

        this.quantity = newQuantity;

        // 변화 방향에 따라 상태 변경
        if (change > 0) {
            updateStockHistory(StockStatus.STOCK_INCREASED, change);
        } else {
            updateStockHistory(StockStatus.STOCK_DECREASED, change);
        }
    }

    // 재고 증가
    public void increaseStock(int amount) {
        this.quantity += amount;
        updateStockHistory(StockStatus.STOCK_INCREASED, amount);
    }

    // 재고 감소
    public void decreaseStock(int amount) {
        this.quantity -= amount;
        updateStockHistory(StockStatus.STOCK_DECREASED, -amount);
    }

    // 재고 입고 처리
    public void inboundStock(int amount) {
        this.quantity += amount;
        updateStockHistory(StockStatus.INBOUND, amount);
    }

    // 재고 출고 처리
    public void outboundStock(int amount) {
        this.quantity -= amount;
        updateStockHistory(StockStatus.OUTBOUND, -amount);
    }

    // 품절 처리
    public void soldOut() {
        this.quantity = 0;
        updateStockHistory(StockStatus.SOLD_OUT, 0);
    }

}
