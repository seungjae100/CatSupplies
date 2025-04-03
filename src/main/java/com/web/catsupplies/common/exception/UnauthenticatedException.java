package com.web.catsupplies.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthenticatedException extends  RuntimeException {
    // 401 인증 실패 (Unauthorized)
    public UnauthenticatedException(String message) {
        super(message);
    }
}
