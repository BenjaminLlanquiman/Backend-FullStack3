package com.GrupoCordillera.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    @NotBlank private String nombre;
    @NotBlank private String rol; // ADMIN, VENDEDOR, BODEGUERO, VIEWER
}