package com.grupocordillera.inventario.service.interfaces;

import com.grupocordillera.inventario.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface InventarioService {

    // Productos
    ProductoResponseDTO crearProducto(ProductoRequestDTO request);
    ProductoResponseDTO obtenerProductoPorId(Long id);
    ProductoResponseDTO obtenerProductoPorCodigo(String codigo);
    List<ProductoResponseDTO> listarProductos();
    List<ProductoResponseDTO> listarPorCategoria(String categoria);
    List<ProductoResponseDTO> listarPorSucursal(Long sucursalId);
    List<ProductoResponseDTO> listarConStockBajo();
    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO request);
    void eliminarProducto(Long id);

    // Movimientos
    MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO request);
    List<MovimientoResponseDTO> obtenerMovimientosPorProducto(Long productoId);
    List<MovimientoResponseDTO> obtenerMovimientosEntreFechas(LocalDateTime inicio, LocalDateTime fin);
}