package com.grupocordillera.inventario.service.impl;

import com.grupocordillera.inventario.dto.*;
import com.grupocordillera.inventario.exception.ProductoNotFoundException;
import com.grupocordillera.inventario.exception.StockInsuficienteException;
import com.grupocordillera.inventario.model.MovimientoStock;
import com.grupocordillera.inventario.model.Producto;
import com.grupocordillera.inventario.repository.MovimientoStockRepository;
import com.grupocordillera.inventario.repository.ProductoRepository;
import com.grupocordillera.inventario.service.interfaces.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final ProductoRepository productoRepository;
    private final MovimientoStockRepository movimientoRepository;

    @Override
    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        Producto producto = Producto.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .stockMinimo(request.getStockMinimo())
                .categoria(request.getCategoria())
                .sucursalId(request.getSucursalId())
                .activo(true)
                .build();
        return toResponseDTO(productoRepository.save(producto));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con código: " + codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findByActivoTrue()
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarPorSucursal(Long sucursalId) {
        return productoRepository.findBySucursalId(sucursalId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarConStockBajo() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getActivo() && p.getStock() <= p.getStockMinimo())
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setCategoria(request.getCategoria());

        return toResponseDTO(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + request.getProductoId()));

        int stockAnterior = producto.getStock();
        int stockResultante;

        switch (request.getTipoMovimiento().toUpperCase()) {
            case "ENTRADA":
                stockResultante = stockAnterior + request.getCantidad();
                break;
            case "SALIDA":
                if (stockAnterior < request.getCantidad()) {
                    throw new StockInsuficienteException(
                        "Stock insuficiente. Disponible: " + stockAnterior + ", solicitado: " + request.getCantidad());
                }
                stockResultante = stockAnterior - request.getCantidad();
                break;
            case "AJUSTE":
                stockResultante = request.getCantidad();
                break;
            default:
                throw new IllegalArgumentException("Tipo de movimiento inválido: " + request.getTipoMovimiento());
        }

        producto.setStock(stockResultante);
        productoRepository.save(producto);

        MovimientoStock movimiento = MovimientoStock.builder()
                .productoId(producto.getId())
                .tipoMovimiento(request.getTipoMovimiento().toUpperCase())
                .cantidad(request.getCantidad())
                .stockAnterior(stockAnterior)
                .stockResultante(stockResultante)
                .motivo(request.getMotivo())
                .fechaMovimiento(LocalDateTime.now())
                .build();

        return toMovimientoDTO(movimientoRepository.save(movimiento));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> obtenerMovimientosPorProducto(Long productoId) {
        return movimientoRepository.findByProductoId(productoId)
                .stream().map(this::toMovimientoDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> obtenerMovimientosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaMovimientoBetween(inicio, fin)
                .stream().map(this::toMovimientoDTO).collect(Collectors.toList());
    }

    private ProductoResponseDTO toResponseDTO(Producto p) {
        return ProductoResponseDTO.builder()
                .id(p.getId())
                .codigo(p.getCodigo())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .stockMinimo(p.getStockMinimo())
                .categoria(p.getCategoria())
                .sucursalId(p.getSucursalId())
                .activo(p.getActivo())
                .stockBajo(p.getStock() <= p.getStockMinimo())
                .build();
    }

    private MovimientoResponseDTO toMovimientoDTO(MovimientoStock m) {
        return MovimientoResponseDTO.builder()
                .id(m.getId())
                .productoId(m.getProductoId())
                .tipoMovimiento(m.getTipoMovimiento())
                .cantidad(m.getCantidad())
                .stockAnterior(m.getStockAnterior())
                .stockResultante(m.getStockResultante())
                .motivo(m.getMotivo())
                .fechaMovimiento(m.getFechaMovimiento())
                .build();
    }
}