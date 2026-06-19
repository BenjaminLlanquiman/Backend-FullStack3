package com.grupocordillera.ventas.model;

import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Table(name = "venta_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
 
    @Column(nullable = false)
    private Long productoId;
 
    @Column(nullable = false)
    private String nombreProducto;
 
    @Column(nullable = false)
    private Integer cantidad;
 
    @Column(nullable = false)
    private Double precioUnitario;
 
    @Column(nullable = false)
    private Double subtotal;
}
