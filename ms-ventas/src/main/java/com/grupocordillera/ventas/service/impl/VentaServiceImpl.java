package com.grupocordillera.ventas.service.impl;

import com.grupocordillera.ventas.client.InventarioClient;
import com.grupocordillera.ventas.dto.*;
import com.grupocordillera.ventas.exception.VentaNotFoundException;
import com.grupocordillera.ventas.model.Venta;
import com.grupocordillera.ventas.model.VentaDetalle;
import com.grupocordillera.ventas.repository.VentaRepository;
import com.grupocordillera.ventas.service.interfaces.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
 
@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {
 
    private final VentaRepository ventaRepository;
    private final InventarioClient inventarioClient;
 
    @Override
    @Transactional
    public VentaResponseDTO crearVenta(VentaRequestDTO request) {
        // Calcular totales desde los detalles
        double subtotal = request.getDetalles().stream()
                .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                .sum();
        double impuesto = Math.round(subtotal * 0.19 * 100.0) / 100.0; // IVA 19%
        double total = subtotal + impuesto;
 
        Venta venta = Venta.builder()
                .codigoVenta(generarCodigoVenta())
                .clienteId(request.getClienteId())
                .sucursalId(request.getSucursalId())
                .subtotal(subtotal)
                .impuesto(impuesto)
                .total(total)
                .metodoPago(request.getMetodoPago())
                .estado(request.getEstado())
                .fechaVenta(LocalDateTime.now())
                .build();
 
        // Crear detalles
        List<VentaDetalle> detalles = request.getDetalles().stream()
                .map(d -> VentaDetalle.builder()
                        .venta(venta)
                        .productoId(d.getProductoId())
                        .nombreProducto(d.getNombreProducto())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getPrecioUnitario() * d.getCantidad())
                        .build())
                .collect(Collectors.toList());
 
        venta.setDetalles(detalles);
        Venta saved = ventaRepository.save(venta);
 
        // Descontar stock de cada producto
        request.getDetalles().forEach(d ->
            inventarioClient.descontarStock(
                d.getProductoId(),
                d.getCantidad(),
                "Venta " + saved.getCodigoVenta()
            )
        );
 
        return toResponseDTO(saved);
    }
 
    @Override
    @Transactional
    public void eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con id: " + id));
 
        // Devolver stock al eliminar
        if (venta.getDetalles() != null) {
            venta.getDetalles().forEach(d ->
                inventarioClient.devolverStock(
                    d.getProductoId(),
                    d.getCantidad(),
                    "Devolucion venta " + venta.getCodigoVenta()
                )
            );
        }
 
        ventaRepository.deleteById(id);
    }
 
    @Override
    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con id: " + id));
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarTodasLasVentas() {
        return ventaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorSucursal(Long sucursalId) {
        return ventaRepository.findBySucursalId(sucursalId).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorEstado(String estado) {
        return ventaRepository.findByEstado(estado).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasEntreFechas(java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin).stream()
                .map(this::toResponseDTO).collect(Collectors.toList());
    }
 
    @Override
    @Transactional
    public VentaResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con id: " + id));
        venta.setEstado(nuevoEstado);
        return toResponseDTO(ventaRepository.save(venta));
    }
 
    private String generarCodigoVenta() {
        return "VTA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
 
    private VentaResponseDTO toResponseDTO(Venta venta) {
        List<VentaDetalleResponse> detalles = venta.getDetalles() == null ? List.of() :
                venta.getDetalles().stream()
                        .map(d -> VentaDetalleResponse.builder()
                                .id(d.getId())
                                .productoId(d.getProductoId())
                                .nombreProducto(d.getNombreProducto())
                                .cantidad(d.getCantidad())
                                .precioUnitario(d.getPrecioUnitario())
                                .subtotal(d.getSubtotal())
                                .build())
                        .collect(Collectors.toList());
 
        return VentaResponseDTO.builder()
                .id(venta.getId())
                .codigoVenta(venta.getCodigoVenta())
                .clienteId(venta.getClienteId())
                .sucursalId(venta.getSucursalId())
                .subtotal(venta.getSubtotal())
                .impuesto(venta.getImpuesto())
                .total(venta.getTotal())
                .metodoPago(venta.getMetodoPago())
                .estado(venta.getEstado())
                .fechaVenta(venta.getFechaVenta())
                .detalles(detalles)
                .build();
    }
}