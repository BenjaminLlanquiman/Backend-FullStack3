package com.grupocordillera.inventario.repository;

import com.grupocordillera.inventario.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {

    List<MovimientoStock> findByProductoId(Long productoId);

    List<MovimientoStock> findByTipoMovimiento(String tipoMovimiento);

    List<MovimientoStock> findByFechaMovimientoBetween(LocalDateTime inicio, LocalDateTime fin);
}