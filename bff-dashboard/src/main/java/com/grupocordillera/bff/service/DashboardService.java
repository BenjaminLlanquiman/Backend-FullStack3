package com.grupocordillera.bff.service;

import com.grupocordillera.bff.dto.DashboardDTO;
import com.grupocordillera.bff.dto.ProductoDTO;
import com.grupocordillera.bff.dto.VentaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VentasClientService ventasClient;
    private final InventarioClientService inventarioClient;

    public DashboardDTO obtenerDashboard() {

        // Obtener datos de ventas
        List<VentaDTO> todasLasVentas = ventasClient.obtenerVentas();
        List<VentaDTO> ventasPendientes = ventasClient.obtenerVentasPorEstado("PENDIENTE");
        List<VentaDTO> ventasCompletadas = ventasClient.obtenerVentasPorEstado("COMPLETADA");

        // Obtener datos de inventario
        List<ProductoDTO> todosLosProductos = inventarioClient.obtenerProductos();
        List<ProductoDTO> stockBajo = inventarioClient.obtenerProductosStockBajo();

        // Calcular KPIs
        double montoTotal = todasLasVentas.stream()
                .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
                .sum();

        // Últimas 5 ventas
        List<VentaDTO> ultimasVentas = todasLasVentas.stream()
                .sorted((a, b) -> {
                    if (a.getFechaVenta() == null || b.getFechaVenta() == null) return 0;
                    return b.getFechaVenta().compareTo(a.getFechaVenta());
                })
                .limit(5)
                .toList();

        // Estado de servicios
        String estadoVentas = todasLasVentas.isEmpty() && ventasPendientes.isEmpty()
                ? "DEGRADADO" : "ACTIVO";
        String estadoInventario = todosLosProductos.isEmpty()
                ? "DEGRADADO" : "ACTIVO";

        return DashboardDTO.builder()
                .totalVentas((long) todasLasVentas.size())
                .montoTotalVentas(montoTotal)
                .ventasPendientes((long) ventasPendientes.size())
                .ventasCompletadas((long) ventasCompletadas.size())
                .totalProductos((long) todosLosProductos.size())
                .productosStockBajo((long) stockBajo.size())
                .ultimasVentas(ultimasVentas)
                .productosConStockBajo(stockBajo)
                .estadoServicioVentas(estadoVentas)
                .estadoServicioInventario(estadoInventario)
                .build();
    }
}