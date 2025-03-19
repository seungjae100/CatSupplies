package com.web.catsupplies.product.domain;

public enum StockStatus {

    INBOUND,     // 상품 입고 완료
    OUTBOUND,    // 출고 완료
    PAID,        // 결제 완료 (출고 전)
    SHIPPING,    // 배송 중
    DELIVERED,   // 배송 완료
    SOLD_OUT     // 품절
}
