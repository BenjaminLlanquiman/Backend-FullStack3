package com.grupocordillera.ventas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalleRequest {
 
    @NotNull
    private Long productoId;
 
    @NotBlank
    private String nombreProducto;
 
    @NotNull @Positive
    private Integer cantidad;
 
    @NotNull @Positive
    private Double precioUnitario;
}
