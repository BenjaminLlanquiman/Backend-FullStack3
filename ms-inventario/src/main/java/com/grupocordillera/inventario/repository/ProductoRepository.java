package com.grupocordillera.inventario.repository;

import com.grupocordillera.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByCategoria(String categoria);

    List<Producto> findBySucursalId(Long sucursalId);

    List<Producto> findByActivoTrue();

    // Productos con stock bajo el mínimo (para alertas del dashboard)
    List<Producto> findByStockLessThanEqualAndActivoTrue(Integer stockMinimo);
}