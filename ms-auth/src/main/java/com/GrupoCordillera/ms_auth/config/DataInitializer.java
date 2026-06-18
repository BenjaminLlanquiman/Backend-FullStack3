package com.GrupoCordillera.ms_auth.config;

import com.GrupoCordillera.ms_auth.model.Rol;
import com.GrupoCordillera.ms_auth.model.Usuario;
import com.GrupoCordillera.ms_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
 
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
 
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
 
    @Override
    public void run(String... args) {
        crearUsuarioSiNoExiste("admin",      "admin123",     "Administrador",  Rol.ADMIN);
        crearUsuarioSiNoExiste("vendedor1",  "vendedor123",  "Juan Ventas",    Rol.VENDEDOR);
        crearUsuarioSiNoExiste("bodeguero1", "bodega123",    "Pedro Bodega",   Rol.BODEGUERO);
        crearUsuarioSiNoExiste("viewer1",    "viewer123",    "Ana Viewer",     Rol.VIEWER);
        log.info("✅ Usuarios por defecto inicializados");
    }
 
    private void crearUsuarioSiNoExiste(String username, String password, String nombre, Rol rol) {
        if (!usuarioRepository.existsByUsername(username)) {
            usuarioRepository.save(Usuario.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .nombre(nombre)
                    .rol(rol)
                    .activo(true)
                    .build());
            log.info("Usuario creado: {} ({})", username, rol);
        }
    }
}