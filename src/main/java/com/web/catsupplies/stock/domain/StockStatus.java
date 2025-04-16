package com.web.catsupplies.stock.domain;

public enum StockStatus {

    INBOUND,     // 상품 입고 완료
    OUTBOUND,    // 출고 완료
    SOLD_OUT,     // 품절

    STOCK_INCREASED,  // 재고 증가
    STOCK_DECREASED   // 재고 감소
}
