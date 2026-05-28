package com.grupocordillera.ventas.service.interfaces;

import com.grupocordillera.ventas.dto.VentaRequestDTO;
import com.grupocordillera.ventas.dto.VentaResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaService {

    VentaResponseDTO crearVenta(VentaRequestDTO request);

    VentaResponseDTO obtenerVentaPorId(Long id);

    List<VentaResponseDTO> listarTodasLasVentas();

    List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId);

    List<VentaResponseDTO> obtenerVentasPorSucursal(Long sucursalId);

    List<VentaResponseDTO> obtenerVentasPorEstado(String estado);

    List<VentaResponseDTO> obtenerVentasEntreFechas(LocalDateTime inicio, LocalDateTime fin);

    VentaResponseDTO actualizarEstado(Long id, String nuevoEstado);

    void eliminarVenta(Long id);
}