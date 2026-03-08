package com.backend.ecommercespringbootbackend.config;

import com.backend.ecommercespringbootbackend.entities.*;
import com.backend.ecommercespringbootbackend.upgrades.CustomerLogin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestDataConfig implements RepositoryRestConfigurer {
    @Value("${cors.allowed.origin}")
    private String allowedOrigin;
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        cors.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);

        config.exposeIdsFor(Country.class, Customer.class, Division.class,
                Excursion.class, Vacation.class, CustomerLogin.class);
    }
}