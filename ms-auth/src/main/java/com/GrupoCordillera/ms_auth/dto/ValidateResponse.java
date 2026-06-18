package com.GrupoCordillera.ms_auth.dto;

import lombok.*;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateResponse {
    private boolean valid;
    private String username;
    private String rol;
}