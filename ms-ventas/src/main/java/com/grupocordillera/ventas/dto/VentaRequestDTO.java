package com.grupocordillera.ventas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotNull(message = "La sucursalId es obligatoria")
    private Long sucursalId;

    @NotNull
    @Positive
    private Double subtotal;

    @NotNull
    @PositiveOrZero
    private Double impuesto;

    @NotNull
    @Positive
    private Double total;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}