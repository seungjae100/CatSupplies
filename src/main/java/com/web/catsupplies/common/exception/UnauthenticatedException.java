package com.web.catsupplies.common.exception;

public class UnauthenticatedException extends  RuntimeException {
    // 401 인증 실패 (Unauthorized)
    public UnauthenticatedException(String message) {
        super(message);
    }
}
