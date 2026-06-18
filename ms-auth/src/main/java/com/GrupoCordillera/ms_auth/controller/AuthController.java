package com.GrupoCordillera.ms_auth.controller;

import com.GrupoCordillera.ms_auth.dto.*;
import com.GrupoCordillera.ms_auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.Map;
 
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
 
    private final AuthService authService;
 
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
 
    // Solo ADMIN puede registrar nuevos usuarios
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
 
    // El BFF llama a este endpoint para validar tokens internamente
    @GetMapping("/validate")
    public ResponseEntity<ValidateResponse> validate(@RequestParam String token) {
        return ResponseEntity.ok(authService.validate(token));
    }
 
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ms-auth activo en puerto 8084");
    }
}