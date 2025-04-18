package com.web.catsupplies.common.config;

import com.web.catsupplies.common.exception.ApiErrorResponse;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class GlobalResponseConfig {

    @Bean
    public OpenApiCustomizer globalResponseCustomiser() {
        return openApi -> {
            // ApiErrorResponse 스키마 등록
            Schema<?> errorSchema = new ObjectSchema()
                    .addProperty("status", new IntegerSchema().example(400))
                    .addProperty("error", new StringSchema().example("Bad Request"))
                    .addProperty("message", new StringSchema().example("잘못된 요청입니다."));

            openApi.getComponents().addSchemas("ApiErrorResponse", errorSchema);

            // 전역 예외 응답 추가
            openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    operation.getResponses().addApiResponse("400", createErrorResponse("잘못된 요청입니다.", 400));
                    operation.getResponses().addApiResponse("401", createErrorResponse("인증이 필요합니다.", 401));
                    operation.getResponses().addApiResponse("403", createErrorResponse("권한이 없습니다.", 403));
                    operation.getResponses().addApiResponse("500", createErrorResponse("서버에 에러가 발생했습니다.", 500));
                }));
        };
    }

    private ApiResponse createErrorResponse(String description, int statusCode) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType()
                                .schema(new Schema<ApiErrorResponse>().$ref("#/components/schemas/ApiErrorResponse"))
                                .example(Map.of(
                                        "status", statusCode,
                                        "error", getErrorReason(statusCode),
                                        "message", description
                                ))));
    }


    private String getErrorReason(int code) {
        return switch (code) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }
}
