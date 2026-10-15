# Caché vs. Base de Datos con Spring Boot y JPA

Este proyecto compara el tiempo que tarda una aplicación en consultar 300.000 productos en una base de datos, utilizando dos métodos: una consulta normal con JPA y otra que aprovecha la caché de Spring mediante `@Cacheable`.

Para visualizar la comparación, se incluye una página web sencilla que permite ejecutar las pruebas y observar los resultados.

## Tecnologías utilizadas

- **Java y Spring Boot:** para desarrollar el backend.
- **Spring Data JPA:** para acceder a los datos.
- **H2:** como base de datos.
- **HTML y JavaScript:** para la interfaz web.
- **Maven:** para gestionar las dependencias del proyecto.

## Estructura del proyecto

- `backend`: contiene la aplicación Spring Boot, la lógica de consulta y la configuración de la caché.
- `frontend`: contiene la página web que muestra los resultados de las pruebas.

## Resultados de las pruebas

Se comparan tres lecturas: una sin caché, la primera con caché y una segunda lectura que reutiliza los datos almacenados.

| Prueba | Tiempo (ms) |
|---|---:|
| Sin caché | — |
| Con caché (primera lectura) | — |
| Con caché (segunda lectura) | — |

Los tiempos se completarán después de ejecutar las pruebas. Se espera que la segunda lectura con caché sea más rápida, ya que evita repetir la consulta a la base de datos.

## Endpoints principales

- `GET /api/productos/sin-cache`: consulta los productos directamente desde la base de datos.
- `GET /api/productos/con-cache`: consulta los productos utilizando caché.
- `DELETE /api/cache`: limpia la caché.
- `GET /api/comparar`: ejecuta la comparación completa.

## CORS: Problma y como se soluciono

El frontend (`localhost:5500`) y el backend (`localhost:8080`) utilizan puertos diferentes, por lo que el navegador los considera orígenes distintos. Esto puede provocar que bloquee las solicitudes del frontend por seguridad.

Para solucionarlo, se configuró `CorsConfig.java`, que permite las solicitudes desde `localhost` hacia las rutas de la API. Así, la página puede consultar los datos del backend y mostrar los tiempos de las pruebas.

Esta configuración se puede activar o desactivar mediante `app.cors.habilitado` en `application.properties`. Está pensada para desarrollo local; en producción, lo recomendable es permitir únicamente los orígenes autorizados.

La API devuelve la cantidad de productos consultados, el tiempo de ejecución y una muestra de cinco registros, evitando enviar los 300.000 productos al navegador.
