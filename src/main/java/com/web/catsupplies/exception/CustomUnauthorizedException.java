package com.web.catsupplies.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 상태 코드 설정
public class CustomUnauthorizedException extends RuntimeException{
    public CustomUnauthorizedException(String message) {
        super(message);
    }
}
