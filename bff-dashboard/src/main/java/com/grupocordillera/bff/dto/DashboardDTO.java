package com.grupocordillera.bff.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    // KPIs de ventas
    private Long totalVentas;
    private Double montoTotalVentas;
    private Long ventasPendientes;
    private Long ventasCompletadas;

    // KPIs de inventario
    private Long totalProductos;
    private Long productosStockBajo;

    // Listas
    private List<VentaDTO> ultimasVentas;
    private List<ProductoDTO> productosConStockBajo;

    // Estado de los servicios
    private String estadoServicioVentas;
    private String estadoServicioInventario;
}