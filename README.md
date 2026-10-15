# Caché vs. base de datos con Spring Boot + JPA

Aplicación que lee muchísimos registros (300 000 productos) de una base de datos con JPA y compara el tiempo **sin caché** contra **con caché** (`@Cacheable` de Spring). Incluye un front sencillo que muestra la comparación y resuelve el problema de CORS.

```
proyecto-cache/
├── backend/    Spring Boot 3.5 + JPA + H2 (Maven)
└── frontend/   index.html (HTML + JS puro)
```

## Cómo correrlo

### 1. Backend (IntelliJ)
1. `File > Open` y elige la carpeta **`backend`** (IntelliJ detecta el `pom.xml` y descarga las dependencias).
2. Si IntelliJ pide un JDK, usa el que tengas (21 o superior sirve; el proyecto compila para Java 21).
3. Ejecuta la clase `CacheApplication`.
4. Al arrancar verás en la consola `Generando 300,000 registros de prueba...`. Cuando termine, el servidor queda en `http://localhost:8080`.

Para probarlo sin front: abre en el navegador `http://localhost:8080/api/comparar`.

### 2. Frontend
CORS solo aparece si el front se sirve desde otro origen, así que no hagas doble clic al archivo; sírvelo así:

```powershell
cd frontend
python -m http.server 5500
```
y abre `http://localhost:5500`. (Alternativa: en IntelliJ, abre `index.html` y usa el ícono del navegador de la esquina; sirve la página desde `localhost:63342`.)

Pulsa **Prueba completa**.

## Qué se espera ver

| # | Lectura | Qué pasa | Tiempo |
|---|---------|----------|--------|
| 1 | Sin caché | Consulta a la base de datos | lento |
| 2 | Con caché (1ª vez) | Consulta a la base de datos y guarda en memoria | lento, parecido al #1 |
| 3 | Con caché (2ª vez) | Devuelve lo guardado, sin tocar la base de datos | casi 0 ms |

> Pega aquí tus resultados reales cuando lo corras:
>
> | Lectura | Tiempo (ms) |
> |---------|-------------|
> | Sin caché | |
> | Con caché, 1ª vez | |
> | Con caché, 2ª vez | |

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/productos/sin-cache` | Lee todo desde la base de datos |
| GET | `/api/productos/con-cache` | Lee todo usando `@Cacheable` |
| DELETE | `/api/cache` | Vacía el caché |
| GET | `/api/comparar` | Corre la prueba completa en el servidor |

La API no devuelve los 300 000 registros (sería medir la red, no la base de datos): devuelve cantidad, tiempo y una muestra de 5.

## El problema de CORS (resumen)

El front está en `localhost:5500` y el backend en `localhost:8080`. Para el navegador son **orígenes distintos** (el puerto cuenta), y bloquea la respuesta a menos que el servidor la autorice con la cabecera `Access-Control-Allow-Origin`.

- **Solución usada (desarrollo):** `CorsConfig.java` permite cualquier puerto de `localhost` hacia `/api/**`.
- **Para verlo fallar:** en `application.properties` pon `app.cors.habilitado=false`, reinicia el backend y recarga el front: la consola (F12) muestra el error de CORS.
- **Otras formas de resolverlo:** `@CrossOrigin` en el controlador, o un proxy en el servidor de desarrollo del front para que el navegador solo hable con su propio origen.

## Subirlo a GitHub

Crea un repositorio vacío en github.com (sin README) y, desde la carpeta `proyecto-cache`:

```powershell
git init
git add .
git commit -m "Comparación de caché con Spring Boot y JPA"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
git push -u origin main
```
