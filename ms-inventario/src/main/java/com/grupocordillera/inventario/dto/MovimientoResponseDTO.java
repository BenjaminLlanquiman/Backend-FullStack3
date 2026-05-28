package com.grupocordillera.inventario.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoResponseDTO {

    private Long id;
    private Long productoId;
    private String tipoMovimiento;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockResultante;
    private String motivo;
    private LocalDateTime fechaMovimiento;
}