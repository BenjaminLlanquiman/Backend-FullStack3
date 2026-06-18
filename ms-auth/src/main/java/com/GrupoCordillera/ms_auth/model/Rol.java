package com.GrupoCordillera.ms_auth.model;

public enum Rol {
    ADMIN,      // Acceso total
    VENDEDOR,   // Dashboard + Ventas
    BODEGUERO,  // Dashboard + Inventario
    VIEWER      // Solo Dashboard (lectura)
}