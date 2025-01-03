package com.algonet.algonetapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private GetAuthUserArgumentResolver getAuthUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // Register the custom argument resolver
        resolvers.add(getAuthUserArgumentResolver);
    }
}
