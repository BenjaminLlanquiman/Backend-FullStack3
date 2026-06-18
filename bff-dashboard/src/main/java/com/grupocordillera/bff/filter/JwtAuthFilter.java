package com.grupocordillera.bff.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
 
import java.io.IOException;
 
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
    @Value("${services.auth-url}")
    private String authUrl;
 
    private final WebClient webClient = WebClient.builder().build();
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
 
        // Health check no requiere auth
        if (request.getRequestURI().contains("/health")) {
            filterChain.doFilter(request, response);
            return;
        }
 
        String authHeader = request.getHeader("Authorization");
 
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }
 
        String token = authHeader.substring(7);
 
        try {
            ValidateResponse validate = webClient.get()
                    .uri(authUrl + "/api/v1/auth/validate?token=" + token)
                    .retrieve()
                    .bodyToMono(ValidateResponse.class)
                    .block();
 
            if (validate == null || !validate.valid()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
                return;
            }
 
            // Pasar rol al header para uso interno
            request.setAttribute("username", validate.username());
            request.setAttribute("rol", validate.rol());
 
            filterChain.doFilter(request, response);
 
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Error de autenticación\"}");
        }
    }
 
    // Inner record para deserializar respuesta del ms-auth
    record ValidateResponse(boolean valid, String username, String rol) {}
}
