package com.GrupoCordillera.ms_auth.service;

import com.GrupoCordillera.ms_auth.dto.*;
import com.GrupoCordillera.ms_auth.model.Rol;
import com.GrupoCordillera.ms_auth.model.Usuario;
import com.GrupoCordillera.ms_auth.repository.UsuarioRepository;
import com.GrupoCordillera.ms_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
@Service
@RequiredArgsConstructor
public class AuthService {
 
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
 
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
 
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
 
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
 
        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name());
 
        return LoginResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol().name())
                .build();
    }
 
    public LoginResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }
 
        Rol rol;
        try {
            rol = Rol.valueOf(request.getRol().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido: " + request.getRol());
        }
 
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .rol(rol)
                .activo(true)
                .build();
 
        usuarioRepository.save(usuario);
 
        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name());
 
        return LoginResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol().name())
                .build();
    }
 
    public ValidateResponse validate(String token) {
        if (!jwtUtil.isTokenValid(token)) {
            return new ValidateResponse(false, null, null);
        }
        return ValidateResponse.builder()
                .valid(true)
                .username(jwtUtil.extractUsername(token))
                .rol(jwtUtil.extractRol(token))
                .build();
    }
}
