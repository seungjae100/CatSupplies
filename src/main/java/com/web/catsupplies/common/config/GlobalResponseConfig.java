package com.web.catsupplies.common.config;

import com.web.catsupplies.common.exception.ApiErrorResponse;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalResponseConfig {

    @Bean
    public OpenApiCustomizer globalResponseCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    operation.getResponses().addApiResponse("400", createErrorResponse("잘못된 요청입니다."));
                    operation.getResponses().addApiResponse("401", createErrorResponse("인증이 필요합니다."));
                    operation.getResponses().addApiResponse("403", createErrorResponse("권한이 없습니다."));
                    operation.getResponses().addApiResponse("500", createErrorResponse("서버에 에러가 발생했습니다."));
                }));
    }

    private ApiResponse createErrorResponse(String description) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<ApiErrorResponse>().$ref("#/components/schemas/ApiErrorResponse"))));
    }
}
