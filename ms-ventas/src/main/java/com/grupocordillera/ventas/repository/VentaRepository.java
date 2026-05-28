package com.grupocordillera.ventas.repository;

import com.grupocordillera.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstado(String estado);

    List<Venta> findByClienteId(Long clienteId);

    List<Venta> findBySucursalId(Long sucursalId);

    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
}