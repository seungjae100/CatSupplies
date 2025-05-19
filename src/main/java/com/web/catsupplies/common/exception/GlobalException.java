package com.web.catsupplies.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    // 400 잘못된 요청
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException error) {
        return ResponseEntity.badRequest().body(
                ApiErrorResponse.of(HttpStatus.BAD_REQUEST, error.getMessage())
        );
    }

    // 401 인증 실패 (로그인하지 않은 상태)
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthenticatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiErrorResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage())
        );
    }

    // 403 권한 없음
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccesDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiErrorResponse.of(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    // 404 리소스를 찾을 수 없음
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiErrorResponse.of(HttpStatus.NOT_FOUND, e.getMessage())
        );
    }

    // 500 서버 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleInternalError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.")
        );
    }


}
