package org.elis.event_booking.config;

import org.elis.event_booking.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(
                """
                ROLE_SUPER_ADMIN > ROLE_ADMIN
                ROLE_ADMIN > ROLE_SELLER
                ROLE_SELLER > ROLE_CUSTOMER
                """
        );
        return hierarchy;
    }

    // bean used for method security
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                                .requestMatchers(POST, "/api/v1/auth/register-admin").hasRole("SUPER_ADMIN")
                                .requestMatchers(POST, "/api/v1/auth/register-seller").hasRole("ADMIN")
                                .requestMatchers(POST, "/api/v1/auth/register-customer").permitAll()
                                .requestMatchers(POST, "/api/v1/auth/login").permitAll()

                                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                                .requestMatchers(POST, "/api/v1/categories").hasRole("SELLER")
                                .requestMatchers(GET, "/api/v1/categories").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/categories/*").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/categories/events/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/categories/delete").hasRole("SELLER")

                                .requestMatchers(POST, "/api/v1/events").hasRole("SELLER")
                                .requestMatchers(GET, "/api/v1/events").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/events/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/events/delete").hasRole("SELLER")

                                .requestMatchers(POST, "/api/v1/event-dates").hasRole("SELLER")
                                .requestMatchers(GET, "/api/v1/event-dates").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/event-dates/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/event-dates/delete").hasRole("SELLER")

                                .requestMatchers(POST, "/api/v1/zones").hasRole("SELLER")
                                .requestMatchers(GET, "/api/v1/zones").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/zones/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/zones/delete").hasRole("SELLER")

                                .requestMatchers(POST, "/api/v1/seats").hasRole("SELLER")
                                .requestMatchers(GET, "/api/v1/seats").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/seats/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/seats/edit-price").hasRole("SELLER")
                                .requestMatchers("/api/v1/seats/delete").hasRole("SELLER")

                                .requestMatchers(POST, "/api/v1/places").hasRole("ADMIN")
                                .requestMatchers(GET, "/api/v1/places").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/places/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/places/delete").hasRole("ADMIN")

                                .requestMatchers(GET, "/api/v1/carts").hasRole("ADMIN")
                                .requestMatchers(GET, "/api/v1/carts/user").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/carts/*").hasRole("ADMIN")
                                .requestMatchers("/api/v1/carts/add-seat").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/carts/delete-seat").hasRole("CUSTOMER")

                                .requestMatchers(POST, "/api/v1/bookings").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/bookings").hasRole("ADMIN")
                                .requestMatchers(GET, "/api/v1/bookings/user").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/bookings/event").hasRole("SELLER")
                                .requestMatchers(GET, "/api/v1/bookings/*").hasRole("ADMIN")
                                .requestMatchers("/api/v1/bookings/disable").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/bookings/delete").hasRole("CUSTOMER")

                                .requestMatchers(POST, "/api/v1/reviews").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/reviews").hasRole("CUSTOMER")
                                .requestMatchers(GET, "/api/v1/reviews/*").hasRole("CUSTOMER")
                                .requestMatchers("/api/v1/reviews/delete").hasRole("CUSTOMER")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // I handle the exceptions: AuthenticationException, AccessDeniedException
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException))
                        .accessDeniedHandler((request, response, accessDeniedException) -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException))
                );

        return http.build();
    }
}
