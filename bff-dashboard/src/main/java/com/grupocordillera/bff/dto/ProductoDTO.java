package com.grupocordillera.bff.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Double precio;
    private Integer stock;
    private Integer stockMinimo;
    private String categoria;
    private Long sucursalId;
    private Boolean activo;
    private Boolean stockBajo;
}