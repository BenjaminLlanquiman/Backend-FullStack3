package com.grupocordillera.inventario.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull @Positive
    private Double precio;

    @NotNull @PositiveOrZero
    private Integer stock;

    @NotNull @PositiveOrZero
    private Integer stockMinimo;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotNull
    private Long sucursalId;
}