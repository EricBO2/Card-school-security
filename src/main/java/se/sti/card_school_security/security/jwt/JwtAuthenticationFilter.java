package se.sti.card_school_security.security.jwt;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import se.sti.card_school_security.model.CustomUserDetailsService;
import se.sti.card_school_security.model.authority.UserRole;

import java.util.List;
import java.util.Set;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        // 1. Hämta token (header eller cookie)
        String token = jwtUtils.extractJwtFromRequest(exchange.getRequest());
        if (token == null) {
            token = jwtUtils.extractJwtFromCookie(exchange.getRequest());
        }

        // Ingen token → fortsätt ej inloggad
        if (token == null) {
            return chain.filter(exchange);
        }

        // 2. Validera token
        if (!jwtUtils.validateJwtToken(token)) {
            // ogiltig token → fortsätt som anonym istället för Mono.empty()
            return chain.filter(exchange);
        }

        // 3. Hämta data från JWT
        String email = jwtUtils.getEmailFromJwtToken(token);
        Set<UserRole> roles = jwtUtils.getRolesFromJwtToken(token);

        // Konvertera roller till GrantedAuthority
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();

        Authentication auth =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        // 4. Lägg auth i Reactive Security Context
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }
}
