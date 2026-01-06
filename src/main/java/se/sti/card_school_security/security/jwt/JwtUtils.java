package se.sti.card_school_security.security.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

//TODO rs istället för hs
@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    private final SecretKey key;
    private final int jwtExpirationInMs = (int) TimeUnit.MINUTES.toMillis(10);
    private final JwtParser parser;


    public JwtUtils(@Value("${app.security-key}") String base64EncodedSecretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
        parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public Mono<Claims> parseToken(String token) {
        return Mono.fromCallable(() ->
                parser.parseClaimsJws(token).getBody()
        ).subscribeOn(Schedulers.boundedElastic());
    }


    public Mono<String> generateJwtToken(UserDetails UserDetails) {
        log.debug("Generate JWT token for {} with value: {}", UserDetails.getUsername(),UserDetails.getAuthorities());
        System.out.println("debug JWTToken: " + UserDetails.getUsername());


        List<String> roles = UserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .setSubject(UserDetails.getUsername()) // istället för claim("sub", ...)
                .claim("authorities", roles)
                .setIssuedAt(new Date()) // här använder vi setIssuedAt istället
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key) // key är typ SecretKey
                .compact();
        log.debug("JWT generation successfully for user: {}", UserDetails.getUsername());
        return Mono.just(token);
    }


    public String extractJwtFromCookie(ServerHttpRequest request){
        if (request.getCookies() == null) return null;

        // "token" är namnet på cookie:n
        HttpCookie cookie = request.getCookies().getFirst("token");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public String extractJwtFromRequest(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
