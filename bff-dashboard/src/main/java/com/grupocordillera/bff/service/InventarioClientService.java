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

    @CircuitBreaker(name = "inventario-cb", fallbackMethod = "fallbackEliminar")
    public void eliminarProducto(Long id) {
        inventarioWebClient.delete()
                .uri("/api/v1/inventario/productos/" + id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    @CircuitBreaker(name = "inventario-cb", fallbackMethod = "fallbackActualizar")
    public ProductoDTO actualizarProducto(Long id, ProductoDTO producto) {
        return inventarioWebClient.put()
                .uri("/api/v1/inventario/productos/" + id)
                .bodyValue(producto)
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();
    }

    public List<ProductoDTO> fallbackProductos(Exception e) {
        log.warn("Circuit Breaker activo para ms-inventario: {}", e.getMessage());
        return Collections.emptyList();
    }

    public List<ProductoDTO> fallbackStockBajo(Exception e) {
        log.warn("Circuit Breaker activo para ms-inventario/stock-bajo: {}", e.getMessage());
        return Collections.emptyList();
    }

    public void fallbackEliminar(Long id, Exception e) {
        log.warn("Circuit Breaker activo al eliminar producto: {}", e.getMessage());
    }

    public ProductoDTO fallbackActualizar(Long id, ProductoDTO producto, Exception e) {
        log.warn("Circuit Breaker activo al actualizar producto: {}", e.getMessage());
        return null;
    }
}