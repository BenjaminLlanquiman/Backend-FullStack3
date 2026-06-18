package com.GrupoCordillera.ms_auth.dto;

import lombok.*;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private String nombre;
    private String rol;
}