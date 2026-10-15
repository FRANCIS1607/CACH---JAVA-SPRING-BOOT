package com.ejemplo.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**Solucionamos el problema de COR permitiendo la conexion desde cualquer puerto**/
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.habilitado:true}")
    private boolean habilitado;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (!habilitado) {
            return;
        }
        registry.addMapping("/api/**")
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "DELETE", "OPTIONS");
    }
}
