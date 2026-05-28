package com.grupocordillera.bff.controller;

import com.grupocordillera.bff.dto.DashboardDTO;
import com.grupocordillera.bff.dto.ProductoDTO;
import com.grupocordillera.bff.dto.VentaDTO;
import com.grupocordillera.bff.service.DashboardService;
import com.grupocordillera.bff.service.InventarioClientService;
import com.grupocordillera.bff.service.VentasClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final VentasClientService ventasClient;
    private final InventarioClientService inventarioClient;

    // Endpoint principal del dashboard — agrega todo
    @GetMapping
    public ResponseEntity<DashboardDTO> obtenerDashboard() {
        return ResponseEntity.ok(dashboardService.obtenerDashboard());
    }

    // Proxy a ms-ventas
    @GetMapping("/ventas")
    public ResponseEntity<List<VentaDTO>> listarVentas() {
        return ResponseEntity.ok(ventasClient.obtenerVentas());
    }

    @GetMapping("/ventas/estado/{estado}")
    public ResponseEntity<List<VentaDTO>> ventasPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(ventasClient.obtenerVentasPorEstado(estado));
    }

    // Proxy a ms-inventario
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return ResponseEntity.ok(inventarioClient.obtenerProductos());
    }

    @GetMapping("/productos/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> productosStockBajo() {
        return ResponseEntity.ok(inventarioClient.obtenerProductosStockBajo());
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("BFF Dashboard activo en puerto 8083");
    }
}