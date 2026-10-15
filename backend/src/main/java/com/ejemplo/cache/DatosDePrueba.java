package com.ejemplo.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** LLenamos la tabla con datos aleatorios
 * y usamos JDBC en lotes porque con JPA sería muchommas lento y en este
 * caso lo qeu queremos medir es el tiempo de ejecucion no esto.
 */
@Component
public class DatosDePrueba implements CommandLineRunner {

    private static final String[] CATEGORIAS = {
            "Electronica", "Hogar", "Ropa", "Deportes", "Juguetes", "Libros", "Alimentos", "Belleza"
    };

    private final JdbcTemplate jdbc;
    private final ProductoRepository repositorio;

    @Value("${app.registros:300000}")
    private int total;

    public DatosDePrueba(JdbcTemplate jdbc, ProductoRepository repositorio) {
        this.jdbc = jdbc;
        this.repositorio = repositorio;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() > 0) {
            return;
        }

        System.out.printf("Generando %,d registros de prueba...%n", total);
        long inicio = System.currentTimeMillis();

        Random azar = new Random(42); // semilla fija: siempre los mismos datos
        int tamanoLote = 10_000;

        for (int desde = 0; desde < total; desde += tamanoLote) {
            int cuantos = Math.min(tamanoLote, total - desde);
            List<Object[]> filas = new ArrayList<>(cuantos);
            for (int i = 1; i <= cuantos; i++) {
                filas.add(new Object[] {
                        "Producto " + (desde + i),
                        CATEGORIAS[azar.nextInt(CATEGORIAS.length)],
                        Math.round(azar.nextDouble() * 50_000) / 100.0,
                        azar.nextInt(500)
                });
            }
            jdbc.batchUpdate(
                    "INSERT INTO productos (nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)",
                    filas);
        }

        System.out.printf("Listo: %,d registros en %d ms%n",
                repositorio.count(), System.currentTimeMillis() - inicio);
    }
}
