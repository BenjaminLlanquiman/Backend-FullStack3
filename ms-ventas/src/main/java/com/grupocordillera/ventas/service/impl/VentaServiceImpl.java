package com.grupocordillera.ventas.service.impl;

import com.grupocordillera.ventas.dto.VentaRequestDTO;
import com.grupocordillera.ventas.dto.VentaResponseDTO;
import com.grupocordillera.ventas.exception.VentaNotFoundException;
import com.grupocordillera.ventas.model.Venta;
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

    @Override
    @Transactional
    public VentaResponseDTO crearVenta(VentaRequestDTO request) {
        Venta venta = Venta.builder()
                .codigoVenta(generarCodigoVenta())
                .clienteId(request.getClienteId())
                .sucursalId(request.getSucursalId())
                .subtotal(request.getSubtotal())
                .impuesto(request.getImpuesto())
                .total(request.getTotal())
                .metodoPago(request.getMetodoPago())
                .estado(request.getEstado())
                .fechaVenta(LocalDateTime.now())
                .build();

        return toResponseDTO(ventaRepository.save(venta));
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
        return ventaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorSucursal(Long sucursalId) {
        return ventaRepository.findBySucursalId(sucursalId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorEstado(String estado) {
        return ventaRepository.findByEstado(estado)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VentaResponseDTO actualizarEstado(Long id, String nuevoEstado) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con id: " + id));
        venta.setEstado(nuevoEstado);
        return toResponseDTO(ventaRepository.save(venta));
    }

    @Override
    @Transactional
    public void eliminarVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new VentaNotFoundException("Venta no encontrada con id: " + id);
        }
        ventaRepository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────

    private String generarCodigoVenta() {
        return "VTA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private VentaResponseDTO toResponseDTO(Venta venta) {
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
                .build();
    }
}