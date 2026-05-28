package com.grupocordillera.ventas.controller;

import com.grupocordillera.ventas.dto.VentaRequestDTO;
import com.grupocordillera.ventas.dto.VentaResponseDTO;
import com.grupocordillera.ventas.service.interfaces.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@Valid @RequestBody VentaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.crearVenta(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerVentaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodasLasVentas());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VentaResponseDTO>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ventaService.obtenerVentasPorCliente(clienteId));
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<VentaResponseDTO>> porSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(ventaService.obtenerVentasPorSucursal(sucursalId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VentaResponseDTO>> porEstado(@PathVariable String estado) {
        return ResponseEntity.ok(ventaService.obtenerVentasPorEstado(estado));
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<VentaResponseDTO>> porRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(ventaService.obtenerVentasEntreFechas(inicio, fin));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(ventaService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}