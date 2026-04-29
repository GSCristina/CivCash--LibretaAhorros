# 🏦 SaveLog - Libreta de Ahorros Standalone

**SaveLog** es una aplicación de escritorio desarrollada en Java para la gestión integral de finanzas personales. Permite a los usuarios realizar un seguimiento detallado de sus ingresos y gastos, organizar sus ahorros en diferentes libretas y categorizar cada movimiento para un análisis financiero eficiente.

## 👤 Autor
* **Nombre:** Cristina Cívico Ariza
* **Ciclo:** Desarrollo de Aplicaciones Multiplataforma (DAM)
* **Curso:** 2025/2026

---

## 🛠️ Tecnologías Utilizadas
* **Lenguaje:** Java 17+
* **Interfaz Gráfica:** JavaFX (MVC)
* **Gestión de Proyectos:** Maven
* **Persistencia:** MySQL / MariaDB
* **Serialización:** JAXB para configuración XML
* **Control de Versiones:** Git / GitHub

---

## 📋 Requisitos del Sistema
1. **JDK 17** o superior.
2. **MySQL Server** 8.0 o superior (o MariaDB).
3. **Maven** instalado en el sistema.

---

## ⚙️ Instrucciones de Instalación y Configuración

### 1. Base de Datos
Importa el script SQL que se encuentra en la carpeta `/docs/database_schema.sql` en tu servidor MySQL para crear la estructura de tablas necesaria.

### 2. Configuración de la Conexión
Por motivos de seguridad, los archivos que contienen credenciales no están incluidos en el repositorio. Sigue estos pasos:
1. Localiza el archivo `src/main/resources/db_config.xml.dist`.
2. Haz una copia del archivo y cámbiale el nombre a `db_config.xml`.
3. Abre `db_config.xml` y edita los valores correspondientes a tu entorno (host, puerto, nombre de la base de datos, usuario y contraseña).
```xml
<!-- Ejemplo de configuración en db_config.xml -->
<databaseConfig>
    <host>localhost</host>
    <port>3306</port>
    <dbName>savelog_db</dbName>
    <user>root</user>
    <password>tu_contraseña_aqui</password>
</databaseConfig>
