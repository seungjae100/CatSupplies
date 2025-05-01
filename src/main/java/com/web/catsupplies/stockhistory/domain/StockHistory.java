package com.web.catsupplies.stockhistory.domain;

import com.web.catsupplies.stock.domain.Stock;
import com.web.catsupplies.stock.domain.StockStatus;
import com.web.catsupplies.user.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StockHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantityChange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockStatus stockStatus;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // Stock 과의 연관관게 편의 메서드
    public void setStock(Stock stock) {
        this.stock = stock;
    }

    // 이력 생성 메서드
    public static StockHistory createHistory(Stock stock, StockStatus status, int quantityChange) {
        StockHistory history = new StockHistory();

        history.stockStatus = status;
        history.quantityChange = quantityChange;

        return history;
    }
}
