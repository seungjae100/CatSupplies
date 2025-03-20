package com.web.catsupplies.product.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantityChange;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockStatus stockStatus;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // Stock 과의 연관관게 편의 메서드
    public void setStock(Stock stock) {
        this.stock = stock;
        if (!stock.getStockHistories().contains(this)) {
            stock.getStockHistories().add(this);
        }
    }

    // 이력 생성 메서드
    public static StockHistory createHistory(Stock stock, StockStatus status, int quantityChange) {
        return StockHistory.builder()
                .stock(stock)
                .stockStatus(status)
                .quantityChange(quantityChange)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
