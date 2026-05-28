package com.grupocordillera.inventario.controller;

import com.grupocordillera.inventario.dto.*;
import com.grupocordillera.inventario.service.interfaces.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // ── Productos ──────────────────────────────────────────────

    @PostMapping("/productos")
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearProducto(request));
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(inventarioService.listarProductos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.obtenerProductoPorId(id));
    }

    @GetMapping("/productos/codigo/{codigo}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(inventarioService.obtenerProductoPorCodigo(codigo));
    }

    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(inventarioService.listarPorCategoria(categoria));
    }

    @GetMapping("/productos/sucursal/{sucursalId}")
    public ResponseEntity<List<ProductoResponseDTO>> porSucursal(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(inventarioService.listarPorSucursal(sucursalId));
    }

    @GetMapping("/productos/stock-bajo")
    public ResponseEntity<List<ProductoResponseDTO>> stockBajo() {
        return ResponseEntity.ok(inventarioService.listarConStockBajo());
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(inventarioService.actualizarProducto(id, request));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // ── Movimientos ──────────────────────────────────────────────

    @PostMapping("/movimientos")
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(@Valid @RequestBody MovimientoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.registrarMovimiento(request));
    }

    @GetMapping("/movimientos/producto/{productoId}")
    public ResponseEntity<List<MovimientoResponseDTO>> porProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerMovimientosPorProducto(productoId));
    }

    @GetMapping("/movimientos/rango-fechas")
    public ResponseEntity<List<MovimientoResponseDTO>> porRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(inventarioService.obtenerMovimientosEntreFechas(inicio, fin));
    }
}