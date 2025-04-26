# 🦠 Documentación Técnica - Sistema de Seguimiento COVID-19
**Versión 1.0**


## 📌 1. Introducción
Sistema Java para consultar y almacenar datos epidemiológicos de COVID-19 mediante una API externa, con persistencia en base de datos relacional.



## 🖼️ 13. Capturas de Pantalla

### 🗂️ Pantalla 1: Listado de Regiones  
Muestra todas las regiones obtenidas desde el servicio web, implementando paginación.  
El usuario puede navegar entre páginas (siguiente o anterior).  
Para consultar las provincias de una región, se debe seleccionar la opción `1` y luego ingresar el número correspondiente a la región deseada.

![Captura 1](https://github.com/gjagomez/covid19/blob/main/capturas/1.png)  
![Captura 2](https://github.com/gjagomez/covid19/raw/main/capturas/2.png)  
![Captura 3](https://github.com/gjagomez/covid19/raw/main/capturas/3.png)

---

### 🏙️ Pantalla 2: Listado de Provincias  
Muestra las provincias asociadas a la región seleccionada por el usuario.

![Captura 4](https://github.com/gjagomez/covid19/raw/main/capturas/4.png)

---

### 📊 Pantalla 3: Reportes Epidemiológicos  
Visualiza los reportes detallados de la provincia seleccionada:  
casos confirmados, fallecidos y recuperados.

![Captura 5](https://github.com/gjagomez/covid19/raw/main/capturas/5.png)


## 🖼️ 13. DOCUMENTACION Y REQUISITOS

## ⚙️ 3. Requisitos Técnicos

| 🧱 Componente      | 📦 Versión                      |
|-------------------|-------------------------------|
| ☕ Java            | 8+                            |
| 🧪 Maven           | 3.6+                          |
| 🛢️ Base de Datos   | MySQL 5.7+                    |
| 📚 Dependencias    | Apache HttpClient, Jackson JSON |

---

## ⚙️ 4. Configuración Inicial

### 🗂️ 4.1 Archivo `application.properties`

```properties
# Configuración Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/covid_db
spring.datasource.username=usuario
spring.datasource.password=contraseña

# Configuración API
rapidapi.key=tu-api-key
rapidapi.host=api.covid19data.com
```

---

## 🗃️ 5. Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/umg/covid19/
│   │       ├── controller/
│   │       ├── dto/              # Objetos de Transferencia de Datos
│   │       ├── model/            # Entidades de persistencia
│   │       ├── repository/       # Acceso a datos
│   │       ├── service/          # Lógica de negocio
│   │       ├── util/             # Utilidades comunes
│   │       └── Main.java         # Punto de entrada
│   └── resources/
│       ├── application.properties
│       └── log4j2.xml
```

---

## 🧠 6. Componentes Principales

### 🚀 6.1 Clase Main

```java
public class Main {
    // Flujo principal:
    // 1. Carga regiones con paginación
    // 2. Guarda regiones en DB
    // 3. Carga provincias de región seleccionada
    // 4. Muestra reportes actuales
}
```

### 📡 6.2 CovidDataService

```java
public class CovidDataService {
    // Métodos principales:
    // - fetchRegions(): List<RegionDTO>
    // - fetchProvinces(String iso): List<ProvinceDTO>
    // - fetchReports(String code, String date): List<ReportDTO>
}
```

### 🌐 6.3 ApiClient

```java
public class ApiClient {
    // Configura headers para RapidAPI
    // Maneja serialización JSON a DTOs
    // Métodos principales:
    // - executeGetRequest(): List<T>
}
```

### 💾 6.4 DatabaseHelper

```java
public class DatabaseHelper {
    // Operaciones CRUD básicas:
    // - saveRegion(): Upsert regions
    // - saveProvince(): Upsert provinces
    // - executeUpdate(): Método genérico para queries
}
```

---

## 🔁 7. Flujo de Datos

1. 🔄 Consulta API  
   `Main -> CovidDataService -> ApiClient -> API Externa`
2. 💽 Persistencia  
   `ApiClient -> DatabaseHelper -> MySQL`
3. 🖥️ Interfaz de Usuario  
   `Consola -> Paginación -> Selección -> Visualización`

---



## 🧾 9. Especificación de DTOs

### 🌍 RegionDTO

| Campo | Tipo   | Descripción       |
|-------|--------|-------------------|
| iso   | String | Código ISO región |
| name  | String | Nombre región     |

### 🏙️ ProvinceDTO

| Campo    | Tipo   | Descripción             |
|----------|--------|-------------------------|
| iso      | String | Código ISO región padre |
| code     | String | Código único provincia  |
| province | String | Nombre provincia        |

### 📊 ReportDTO

| Campo     | Tipo      | Descripción         |
|-----------|-----------|---------------------|
| date      | LocalDate | Fecha reporte       |
| confirmed | int       | Casos confirmados   |
| deaths    | int       | Muertes registradas |
| recovered | int       | Casos recuperados   |

---

## 🧯 10. Manejo de Errores

| Escenario         | Manejo                                |
|-------------------|----------------------------------------|
| 🚫 API no disponible | Log en consola + mensaje usuario       |
| 🔌 DB desconectada   | Fallo silencioso (solo log)            |
| 📝 Entrada inválida  | Reintento interactivo                  |

---

## ▶️ 11. Ejecución del Sistema

```bash
mvn clean package
java -jar target/covid19-data-consultation.jar
```

### 📈 Flujo de Consola

1. Seleccionar región con paginación  
2. Elegir provincia  
3. Visualizar reportes del día








## 📌 Anexo Técnico - Códigos de Ejemplo

### 🧾 Método de Paginación

```java
private static <T> T displayItemsWithPagination(List<T> items, String itemType, int pageSize, Scanner scanner) {
    // Lógica de paginación con:
    // - Cálculo de páginas
    // - Navegación interactiva
    // - Validación de entradas
}
```

### 🌐 Consulta API

```java
public List<ReportDTO> fetchReports(String provinceCode, String date) throws Exception {
    String url = "https://" + RAPID_API_HOST + "/reports?iso=" + provinceCode + "&date=2020-04-16";
    return executeGetRequest(url, ReportDTO.class);
}
```

---

**🧑 Elaborado por:** Grupo 5
**🗓️ Fecha:** 25/04/2025 
**Integrantes:
| 🧑‍💼 Nombre           | 🆔 Carnet        |
|----------------------|------------------|
| Javier Gomez Riz     | 1990-07-12940    |
| Oscar Monroy         | 5190-17-10362    |
| Brandon Nij          | 6590-23-19976    |
**📌 Revisión:** 1.0
