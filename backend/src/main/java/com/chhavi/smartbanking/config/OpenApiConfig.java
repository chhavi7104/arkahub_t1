package com.chhavi.smartbanking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI smartBankingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Banking Transaction Management System API")
                        .description("REST API for managing bank accounts and transactions. No authentication required.")
                        .version("v1.0"));
    }
}