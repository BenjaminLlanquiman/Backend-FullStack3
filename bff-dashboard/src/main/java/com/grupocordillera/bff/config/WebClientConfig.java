package com.grupocordillera.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.ventas-url}")
    private String ventasUrl;

    @Value("${services.inventario-url}")
    private String inventarioUrl;

    @Bean
    public WebClient ventasWebClient() {
        return WebClient.builder()
                .baseUrl(ventasUrl)
                .build();
    }

    @Bean
    public WebClient inventarioWebClient() {
        return WebClient.builder()
                .baseUrl(inventarioUrl)
                .build();
    }
}