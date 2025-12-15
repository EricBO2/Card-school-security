package se.sti.card_school_security.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import se.sti.card_school_security.model.CustomUserDetailsService;

import java.util.List;

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

        String token = jwtUtils.extractJwtFromRequest(exchange.getRequest());
        if (token == null) {
            token = jwtUtils.extractJwtFromCookie(exchange.getRequest());
        }

        if (token == null) {
            return chain.filter(exchange);
        }

        return jwtUtils.parseToken(token)
                .map(claims -> {
                    String email = claims.getSubject();

                    Object rawAuthorities = claims.get("authorities");

                    List<GrantedAuthority> authorities = ((List<?>) rawAuthorities).stream()
                            .map(Object::toString)
                            .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                            .toList();

                    return new UsernamePasswordAuthenticationToken(
                            email, null, authorities
                    );
                })
                .flatMap(auth ->
                        chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                )
                .onErrorResume(ex -> chain.filter(exchange));
    }
}
