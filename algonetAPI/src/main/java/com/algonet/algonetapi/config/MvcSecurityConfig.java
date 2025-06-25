package com.algonet.algonetapi.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class MvcSecurityConfig {
    private JwtRequestFilter jwtRequestFilter;
    private CustomUserDetailsService userDetailsService;

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring API security filter chain");
        return http
                .securityMatcher("/api/**")
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/auth/makeUploader").hasAnyRole("ADMIN","UPLOADER","USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/problem").authenticated()
                        .requestMatchers("/api/v1/problem/**").hasAnyRole("UPLOADER", "ADMIN")
                        .requestMatchers("/api/v1/test/**").hasAnyRole("UPLOADER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/tag/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tag/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tag/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/problem-rating").hasAnyRole("UPLOADER", "ADMIN")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring web security filter chain");
        return http
                .securityMatcher("/**")
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(c -> c
                        .requestMatchers("/", "/home", "/login", "/signup", "/problems", "/problems/{id}").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/problems/create", "/problems/{id}/edit", "/profile").authenticated()
                        .requestMatchers("/solutions").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll())                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .csrf(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
