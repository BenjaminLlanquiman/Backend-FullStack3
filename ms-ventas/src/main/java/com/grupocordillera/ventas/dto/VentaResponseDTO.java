package com.grupocordillera.ventas.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponseDTO {
    private Long id;
    private String codigoVenta;
    private Long clienteId;
    private Long sucursalId;
    private Double subtotal;
    private Double impuesto;
    private Double total;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaVenta;
    private List<VentaDetalleResponse> detalles;
}