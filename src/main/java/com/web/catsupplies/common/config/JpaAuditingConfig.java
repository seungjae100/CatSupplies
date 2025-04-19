package com.web.catsupplies.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


// 자동 설정 Config로 분리 SpringBoot메인 코드에서 분리 어노테이션
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
