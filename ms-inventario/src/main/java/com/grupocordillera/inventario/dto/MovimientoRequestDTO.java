package com.grupocordillera.inventario.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoRequestDTO {

    @NotNull
    private Long productoId;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE

    @NotNull @Positive
    private Integer cantidad;

    private String motivo;
}