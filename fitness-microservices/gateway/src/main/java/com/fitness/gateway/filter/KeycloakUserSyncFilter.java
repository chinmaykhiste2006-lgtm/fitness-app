package com.fitness.gateway.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@AllArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter{

    private final ReactiveJwtDecoder jwtDecoder; 

   @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
     
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        requestBuilder.headers(headers -> {
            headers.remove("X-User-Id");
            headers.remove("X-User-Email");
            headers.remove("X-User-First-Name");
            headers.remove("X-User-Last-Name");
        });


        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || token.isBlank()) {
            return unauthorized(exchange, "Missing Authorization header");
        }

        token = token.substring(7).trim();


        return jwtDecoder.decode(token) 
                .flatMap(jwt -> {
                    if (jwt.getSubject() != null) requestBuilder.header("keycloak-id", jwt.getSubject());
                    if (jwt.getClaimAsString("email") != null) requestBuilder.header("email", jwt.getClaimAsString("email"));
                    if (jwt.getClaimAsString("given_name") != null) requestBuilder.header("given-name", jwt.getClaimAsString("given_name"));
                    if (jwt.getClaimAsString("family_name") != null) requestBuilder.header("family-name", jwt.getClaimAsString("family_name"));

                    return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
                })
                .onErrorResume(JwtException.class, e -> {
                    log.warn("JWT verification failed: {}", e.getMessage());
                    return unauthorized(exchange, "Invalid or expired token");
                });

    }


     private Mono<Void> unauthorized(ServerWebExchange exchange, String reason) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        log.warn("Rejecting request to {}: {}", exchange.getRequest().getPath(), reason);
        return exchange.getResponse().setComplete();
    }

}
