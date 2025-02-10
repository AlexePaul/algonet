package com.algonet.algonetapi.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // authentication
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());

        // authorization
        http.authorizeHttpRequests(
                c -> c.requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/auth/makeUploader").hasAnyRole("ADMIN","UPLOADER","USER") // in order to test, will need to be changed in prod

                        .requestMatchers(HttpMethod.GET, "/api/v1/problem").authenticated()
                        .requestMatchers("/api/v1/problem/**").hasAnyRole("UPLOADER", "ADMIN")

                        .requestMatchers("/api/v1/test/**").hasAnyRole("UPLOADER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/v1/tag/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tag/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tag/**").hasRole("ADMIN")

                        .requestMatchers("/api/v1/problem-rating").hasAnyRole("UPLOADER", "ADMIN")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll() // in order to test, will need to be changed in prod
                        .anyRequest().authenticated());

        //csrf
        http.csrf(AbstractHttpConfigurer::disable);

        //cors
        http.cors(Customizer.withDefaults());

        return http.build();
    }

}