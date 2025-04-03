package com.web.catsupplies.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorResponse {

    private int status;

    private String error;

    private String message;

    @Builder
    public ApiErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public static ApiErrorResponse of(HttpStatus status, String message) {
        return ApiErrorResponse.builder()
                .status(status.value()) // "status" : 400,
                .error(status.getReasonPhrase()) // "error" : "Bad Request"
                .message(message)
                .build();
    }

}
