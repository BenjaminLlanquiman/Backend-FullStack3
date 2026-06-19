package com.grupocordillera.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
 
import java.util.Map;
 
@Slf4j
@Component
public class InventarioClient {
 
    @Value("${services.inventario-url}")
    private String inventarioUrl;
 
    private final RestTemplate restTemplate = new RestTemplate();
 
    public void descontarStock(Long productoId, Integer cantidad, String motivo) {
        try {
            String url = inventarioUrl + "/api/v1/inventario/movimientos";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
 
            Map<String, Object> body = Map.of(
                "productoId", productoId,
                "tipoMovimiento", "SALIDA",
                "cantidad", cantidad,
                "motivo", motivo
            );
 
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, Object.class);
            log.info("Stock descontado: producto {} cantidad {}", productoId, cantidad);
        } catch (Exception e) {
            log.error("Error al descontar stock del producto {}: {}", productoId, e.getMessage());
            throw new RuntimeException("Error al descontar stock: " + e.getMessage());
        }
    }
 
    public void devolverStock(Long productoId, Integer cantidad, String motivo) {
        try {
            String url = inventarioUrl + "/api/v1/inventario/movimientos";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
 
            Map<String, Object> body = Map.of(
                "productoId", productoId,
                "tipoMovimiento", "ENTRADA",
                "cantidad", cantidad,
                "motivo", motivo
            );
 
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, request, Object.class);
            log.info("Stock devuelto: producto {} cantidad {}", productoId, cantidad);
        } catch (Exception e) {
            log.error("Error al devolver stock del producto {}: {}", productoId, e.getMessage());
        }
    }
}
