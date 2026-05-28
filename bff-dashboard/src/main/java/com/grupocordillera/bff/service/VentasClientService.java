package com.grupocordillera.bff.service;

import com.grupocordillera.bff.dto.VentaDTO;
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
public class VentasClientService {

    private final WebClient ventasWebClient;

    @CircuitBreaker(name = "ventas-cb", fallbackMethod = "fallbackVentas")
    public List<VentaDTO> obtenerVentas() {
        return ventasWebClient.get()
                .uri("/api/v1/ventas")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<VentaDTO>>() {})
                .block();
    }

    @CircuitBreaker(name = "ventas-cb", fallbackMethod = "fallbackVentasPorEstado")
    public List<VentaDTO> obtenerVentasPorEstado(String estado) {
        return ventasWebClient.get()
                .uri("/api/v1/ventas/estado/{estado}", estado)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<VentaDTO>>() {})
                .block();
    }

    // Fallbacks — retornan datos vacíos si el servicio falla
    public List<VentaDTO> fallbackVentas(Exception e) {
        log.warn("Circuit Breaker activo para ms-ventas: {}", e.getMessage());
        return Collections.emptyList();
    }

    public List<VentaDTO> fallbackVentasPorEstado(String estado, Exception e) {
        log.warn("Circuit Breaker activo para ms-ventas/estado: {}", e.getMessage());
        return Collections.emptyList();
    }
}