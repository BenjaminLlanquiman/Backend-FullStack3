package com.GrupoCordillera.ms_auth.dto;

import lombok.*;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String username;
    private String nombre;
    private String rol;
    private Boolean activo;
}
