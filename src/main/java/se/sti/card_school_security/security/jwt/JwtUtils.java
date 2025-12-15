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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import se.sti.card_school_security.model.CustomUser;
import se.sti.card_school_security.model.CustomUserDetails;
import se.sti.card_school_security.model.authority.UserRole;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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


    public String generateJwtToken(CustomUserDetails customUserDetails) {
        log.debug("Generate JWT token for {} with value: {}", customUserDetails.getUsername(),customUserDetails.getAuthorities());
        System.out.println("debug JWTToken: " + customUserDetails.getUsername());


        List<String> roles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .setSubject(customUserDetails.getUsername()) // istället för claim("sub", ...)
                .claim("authorities", roles)
                .setIssuedAt(new Date()) // här använder vi setIssuedAt istället
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key) // key är typ SecretKey
                .compact();
        log.debug("JWT generation successfully for user: {}", customUserDetails.getUsername());
        return token;
    }


    public String extractJwtFromCookie(ServerHttpRequest request){
        if (request.getCookies() == null) return null;

        // "authToken" är namnet på cookie:n
        HttpCookie cookie = request.getCookies().getFirst("authToken");
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
/*          SPARADE OANVÄNDA METODER SOM HAR RECOMMENDERS ATT SPARAS FÖR FRAMTIDA IMPLEMENTATION
    public String getUsernameFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String username = claims.getSubject(); // Subject, whom the token refers to, principal: whose currently authenticated (system)
            log.debug("Extracted username '{}' from JWT token", username);
            return username;

        } catch (Exception e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public Set<UserRole> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> authoritiesClaim = claims.get("authorities", List.class);

        if (authoritiesClaim == null || authoritiesClaim.isEmpty()) {
            log.warn("No authorities found in JWT token");
            return Set.of();
        }

        // Convert each string like "ROLE_USER" -> UserRole.USER
        Set<UserRole> roles = authoritiesClaim.stream()
                .filter(String.class::isInstance) // keep only strings
                .map(String.class::cast)
                .map(role -> role.replace("ROLE_", "")) // remove prefix if necessary
                .map(String::toUpperCase)
                .map(UserRole::valueOf) // map to your enum
                .collect(Collectors.toSet());

        log.debug("Extracted roles from JWT token: {}", roles);
        return roles;
    }

    public String getEmailFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String email = claims.getSubject();
            log.debug("Extract email '{}' from token", email);
            return email;
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateJwtToken(String authToken) {

        if (authToken == null || authToken.isEmpty()) {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);

            log.debug("Jwt token validated");
            return true;
        } catch (Exception e) {
            log.error("Jwt token validation failed: {}", e.getMessage());
        }
        return false;
    }

 */
}
