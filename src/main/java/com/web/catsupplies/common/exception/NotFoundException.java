package com.web.catsupplies.common.exception;

public class NotFoundException extends RuntimeException {
    // 404 리소스 없음
    public NotFoundException(String message) {
        super(message);
    }
}
