package com.grupocordillera.bff.service;

import com.grupocordillera.bff.dto.ProductoDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioClientService {

    private final WebClient inventarioWebClient;

    @CircuitBreaker(name = "inventario-cb", fallbackMethod = "fallbackProductos")
    public List<ProductoDTO> obtenerProductos() {
        return inventarioWebClient.get()
                .uri("/api/v1/inventario/productos")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductoDTO>>() {})
                .block();
    }

    @CircuitBreaker(name = "inventario-cb", fallbackMethod = "fallbackStockBajo")
    public List<ProductoDTO> obtenerProductosStockBajo() {
        return inventarioWebClient.get()
                .uri("/api/v1/inventario/productos/stock-bajo")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductoDTO>>() {})
                .block();
    }

    // Fallbacks
    public List<ProductoDTO> fallbackProductos(Exception e) {
        log.warn("Circuit Breaker activo para ms-inventario: {}", e.getMessage());
        return Collections.emptyList();
    }

    public List<ProductoDTO> fallbackStockBajo(Exception e) {
        log.warn("Circuit Breaker activo para ms-inventario/stock-bajo: {}", e.getMessage());
        return Collections.emptyList();
    }
}