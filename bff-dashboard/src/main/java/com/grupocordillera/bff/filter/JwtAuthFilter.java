package com.grupocordillera.bff.filter;
 
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import java.io.IOException;
import java.security.Key;
 
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
    @Value("${jwt.secret}")
    private String secret;
 
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
 
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
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
 
            request.setAttribute("username", claims.getSubject());
            request.setAttribute("rol", claims.get("rol", String.class));
 
            filterChain.doFilter(request, response);
 
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Token invalido: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token invalido o expirado\"}");
        }
    }
}