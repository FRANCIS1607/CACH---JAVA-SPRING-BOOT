package com.ejemplo.cache;

import java.util.List;

/**
 * Lo que devuelve la API: no mandamos los 300 mil registros al navegador
 * (sería medir la red, no la base de datos), solo el resumen y una muestra.
 */
public record ResultadoLectura(
        String modo,          // SIN_CACHE o CON_CACHE
        boolean desdeCache,   // true si el resultado ya estaba guardado en caché
        int cantidad,         // cuántos registros se leyeron
        double tiempoMs,      // tiempo medido en el servidor
        double sumaPrecios,   // dato calculado para comprobar que los datos son reales
        List<Producto> muestra) {
}
