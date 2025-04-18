package com.web.catsupplies.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "400")
    private int status;

    @Schema(description = "에러 타입", example = "Bad Request")
    private String error;

    @Schema(description = "에러 메시지", example = "이미 사용중인 이메일입니다.")
    private String message;


    public static ApiErrorResponse of(HttpStatus status, String message) {
        return ApiErrorResponse.builder()
                .status(status.value()) // "status" : 400,
                .error(status.getReasonPhrase()) // "error" : "Bad Request"
                .message(message)
                .build();
    }

}
