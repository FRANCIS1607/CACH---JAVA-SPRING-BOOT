package com.ejemplo.cache;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    public static final String CACHE = "productos";
    public static final String CLAVE = "todos";

    private final ProductoRepository repositorio;

    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Producto> leerSinCache() {
        return repositorio.findAll();
    }

    /**CONSULTA CON CACHÉ : La primera vez se llenará el caché y el tiempo será comparable
       después cuando se ejecute por segunda vez y ya tengo datos preguardados**/
    @Cacheable(cacheNames = CACHE, key = "'" + CLAVE + "'")
    @Transactional(readOnly = true)
    public List<Producto> leerConCache() {
        return repositorio.findAll();
    }
}
