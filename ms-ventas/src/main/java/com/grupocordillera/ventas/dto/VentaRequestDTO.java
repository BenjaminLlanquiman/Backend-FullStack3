package com.grupocordillera.ventas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaRequestDTO {
 
    @NotNull
    private Long clienteId;
 
    @NotNull
    private Long sucursalId;
 
    @NotBlank
    private String metodoPago;
 
    @NotBlank
    private String estado;
 
    @NotNull @NotEmpty
    private List<VentaDetalleRequest> detalles;
}