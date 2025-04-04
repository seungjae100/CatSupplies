package com.web.catsupplies.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    // 404 리소스 없음
    public NotFoundException(String message) {
        super(message);
    }
}
