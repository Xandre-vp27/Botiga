# 🛒 Proyecto Tienda Online (JDBC)

Aplicación de consola en Java que simula la gestión integral de una tienda online, implementando una arquitectura **MVC (Modelo-Vista-Controlador)** y acceso a datos mediante **JDBC**.

Este proyecto demuestra el dominio de bases de datos relacionales con Java, enfocándose especialmente en la integridad de los datos mediante **Transacciones ACID**, control de stock en tiempo real y gestión granular de errores.

## 🚀 Funcionalidades Principales

### 1. Gestión de Datos Maestros (CRUD Completo)
Gestión total de las entidades principales mediante menús interactivos:
* **Clientes:** Alta, listado, modificación y eliminación.
* **Productos:** Gestión de catálogo con control de precios y stock.
* **Descuentos:** Configuración de reglas de descuento (Porcentuales `%` o Fijos `€`) asociadas a productos.

### 2. Gestión Transaccional de Pedidos (Core)
El sistema de creación de pedidos (`ComandaDAO`) es el núcleo del proyecto e implementa lógica compleja:
* **Transacciones ACID:** Uso de `setAutoCommit(false)` para garantizar la atomicidad.
* **Control de Stock:** Decremento automático de stock al confirmar líneas. Si el stock es insuficiente, se realiza un **Rollback automático** de toda la operación.
* **Savepoints:** Intento de aplicación de descuentos. Si la lógica de descuento falla, se realiza un **Rollback parcial** (Savepoint), permitiendo finalizar la venta con el precio original sin perder los datos del pedido.

### 3. Consultas e Informes
* Generación de historiales de compra por cliente utilizando consultas **SQL con JOIN** para vincular tablas (Comandas, Líneas, Productos).

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java (JDK 17+)
* **Base de Datos:** MySQL 8.0
* **Acceso a Datos:** JDBC (Java Database Connectivity) con `PreparedStatement`.
* **Patrón de Diseño:** DAO (Data Access Object) y MVC.
* **Herramientas:** NetBeans / Maven.

## ⚙️ Instalación y Puesta en Marcha

1.  **Base de Datos:**
    * Ejecuta el script `sql/schema.sql` en tu servidor MySQL para crear la base de datos `botiga` y sus tablas.
    * El script incluye datos de prueba iniciales.

2.  **Configuración:**
    * Abre el archivo `src/com/mycompany/botiga/util/Connexio.java`.
    * Configura las constantes `USER` y `PASSWORD` con las credenciales de tu servidor MySQL local.

3.  **Ejecución:**
    * Compila y ejecuta la clase principal: `com.mycompany.botiga.Botiga`.

## 📚 Conceptos Clave Implementados

* **Seguridad:** Uso estricto de `PreparedStatement` para prevenir inyección SQL.
* **Integridad:** Gestión manual de transacciones (`commit`/`rollback`) para asegurar la consistencia de datos críticos (dinero y stock).
* **Robustez:** Manejo de `Savepoints` para tolerancia a fallos parciales dentro de una transacción.
* **Clean Code:** Separación estricta entre lógica de negocio (Modelos), acceso a datos (DAOs) e interfaz de usuario (Vista).

## 👤 Autor

* **Alex** - Desarrollador Full Stack Junior (DAM)
