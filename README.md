# Backend-FullStack3

Repositorio principal del backend de la Plataforma de Monitoreo Inteligente de Grupo Cordillera.
Contiene 3 componentes backend organizados como monorepo.

## Integrantes

- Millaray Tobar
- Maryori Gutierrez
- Monserrat Ampuero
- Benjamin Llanquiman

## Estructura del Proyecto

Backend-FullStack3/
├── ms-ventas/        Microservicio de gestión de ventas
├── ms-inventario/    Microservicio de gestión de inventario
└── bff-dashboard/    Backend For Frontend

## Tecnologías

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- Spring WebFlux
- Resilience4j
- PostgreSQL
- Docker
- Maven

---

## ms-ventas

Microservicio para la gestión de ventas.

### Ejecución local

cd ms-ventas
mvn clean package -DskipTests
java -jar target/ms-ventas-0.0.1-SNAPSHOT.jar

### Endpoints

GET    /api/v1/ventas              - Listar todas las ventas
POST   /api/v1/ventas              - Crear nueva venta
PATCH  /api/v1/ventas/{id}/estado  - Cambiar estado de venta
DELETE /api/v1/ventas/{id}         - Eliminar venta
GET    /api/v1/ventas/estado/{est} - Filtrar por estado

### URL de producción

https://ms-ventas-2c24.onrender.com

---

## ms-inventario

Microservicio para la gestión de inventario y movimientos de stock.

### Ejecución local

cd ms-inventario
mvn clean package -DskipTests
java -jar target/ms-inventario-0.0.1-SNAPSHOT.jar

### Endpoints

GET    /api/v1/inventario/productos            - Listar productos activos
POST   /api/v1/inventario/productos            - Crear producto
PUT    /api/v1/inventario/productos/{id}       - Actualizar producto
DELETE /api/v1/inventario/productos/{id}       - Eliminar producto (soft delete)
GET    /api/v1/inventario/productos/stock-bajo - Productos con stock bajo
POST   /api/v1/inventario/movimientos          - Registrar movimiento de stock

### Tipos de movimiento

- ENTRADA: Aumenta el stock
- SALIDA: Disminuye el stock (valida disponibilidad)
- AJUSTE: Establece el stock al valor indicado

### URL de producción

https://ms-inventario-ar3g.onrender.com

---

## bff-dashboard

Backend For Frontend que agrega datos de ms-ventas y ms-inventario.
Implementa Circuit Breaker con Resilience4j para resiliencia ante fallos.

### Ejecución local

cd bff-dashboard
mvn clean package -DskipTests
java -jar target/bff-dashboard-0.0.1-SNAPSHOT.jar

### Endpoints

GET    /api/v1/dashboard                    - KPIs consolidados del dashboard
GET    /api/v1/dashboard/ventas             - Lista de ventas (proxy)
GET    /api/v1/dashboard/productos          - Lista de productos (proxy)
DELETE /api/v1/dashboard/productos/{id}     - Eliminar producto (proxy)
PUT    /api/v1/dashboard/productos/{id}     - Actualizar producto (proxy)
GET    /api/v1/dashboard/health             - Health check del BFF

### URL de producción

https://bff-dashboard.onrender.com

---

## URLs de Producción

| Servicio      | URL                                              |
|---------------|--------------------------------------------------|
| ms-ventas     | https://ms-ventas-2c24.onrender.com              |
| ms-inventario | https://ms-inventario-ar3g.onrender.com          |
| bff-dashboard | https://bff-dashboard.onrender.com               |
| Frontend      | https://grupocordillera-dashboard-i9zz.onrender.com |
