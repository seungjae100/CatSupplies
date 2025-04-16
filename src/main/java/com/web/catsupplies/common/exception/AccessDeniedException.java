package com.web.catsupplies.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 사용자가 다른 사람의 정보를 수정하거나 접근할 때
// 즉, 인가 실패인 상황에서 발생
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
