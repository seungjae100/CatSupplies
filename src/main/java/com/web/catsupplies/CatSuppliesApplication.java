package com.web.catsupplies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Auditing 기능 활성화
public class CatSuppliesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatSuppliesApplication.class, args);
    }

}
