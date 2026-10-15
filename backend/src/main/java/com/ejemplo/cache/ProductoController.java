package com.ejemplo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductoController {

    private final ProductoService servicio;
    private final CacheManager cacheManager;

    public ProductoController(ProductoService servicio, CacheManager cacheManager) {
        this.servicio = servicio;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/productos/sin-cache")
    public ResultadoLectura sinCache() {
        return medir("SIN_CACHE", false, servicio::leerSinCache);
    }

    @GetMapping("/productos/con-cache")
    public ResultadoLectura conCache() {
        boolean yaEstabaEnCache = cache().get(ProductoService.CLAVE) != null;
        return medir("CON_CACHE", yaEstabaEnCache, servicio::leerConCache);
    }

    @DeleteMapping("/cache")
    public Map<String, String> limpiarCache() {
        cache().clear();
        return Map.of("mensaje", "Caché vaciado");
    }

    /**
     * Corre toda la prueba en el servidor (sin contar la red):
     * 1) sin caché, 2) con caché (1ª vez), 3) con caché (2ª vez), 4) con caché (3ª vez).
     */
    @GetMapping("/comparar")
    public List<ResultadoLectura> comparar() {
        cache().clear();
        List<ResultadoLectura> resultados = new ArrayList<>();
        resultados.add(sinCache());
        resultados.add(conCache());
        resultados.add(conCache());
        resultados.add(conCache());
        return resultados;
    }

    private ResultadoLectura medir(String modo, boolean desdeCache, Supplier<List<Producto>> lectura) {
        long inicio = System.nanoTime();
        List<Producto> productos = lectura.get();
        double tiempoMs = (System.nanoTime() - inicio) / 1_000_000.0;

        double suma = productos.stream().mapToDouble(Producto::getPrecio).sum();
        return new ResultadoLectura(
                modo,
                desdeCache,
                productos.size(),
                redondear(tiempoMs),
                redondear(suma),
                productos.stream().limit(5).toList());
    }

    private Cache cache() {
        Cache cache = cacheManager.getCache(ProductoService.CACHE);
        if (cache == null) {
            throw new IllegalStateException("No existe el caché " + ProductoService.CACHE);
        }
        return cache;
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
