package com.algonet.algonetapi.config;

import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private GetAuthUserArgumentResolver getAuthUserArgumentResolver;    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        // Register the custom argument resolver
        resolvers.add(getAuthUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // Configure CORS for all endpoints
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://78.97.69.149:5173") // Allow only requests from your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
                .allowedHeaders("*") // Allow any headers
                .allowCredentials(true); // Allow sending cookies if necessary
    }
}
