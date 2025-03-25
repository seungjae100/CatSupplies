package com.web.catsupplies.order.domain;

public enum OrderStatus {

    PENDING,     // 주문 생성 완료, 결제 대기 중
    PAID,        // 결제 완료
    CANCELLED    // 주문 취소됨


}
