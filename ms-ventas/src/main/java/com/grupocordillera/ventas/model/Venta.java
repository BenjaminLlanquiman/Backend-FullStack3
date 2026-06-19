package com.grupocordillera.ventas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(unique = true, nullable = false)
    private String codigoVenta;
 
    @Column(nullable = false)
    private Long clienteId;
 
    @Column(nullable = false)
    private Long sucursalId;
 
    @Column(nullable = false)
    private Double subtotal;
 
    @Column(nullable = false)
    private Double impuesto;
 
    @Column(nullable = false)
    private Double total;
 
    @Column(nullable = false)
    private String metodoPago;
 
    @Column(nullable = false)
    private String estado;
 
    @Column(nullable = false)
    private LocalDateTime fechaVenta;
 
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VentaDetalle> detalles;
}