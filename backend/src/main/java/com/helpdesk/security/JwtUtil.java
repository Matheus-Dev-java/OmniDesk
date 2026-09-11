package com.helpdesk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${helpdesk.jwt.secret}")
    private String jwtSecret;

    @Value("${helpdesk.jwt.expiracao-ms}")
    private long jwtExpiracaoMs;

    private SecretKey getChave() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String gerarToken(String email, String role) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + jwtExpiracaoMs);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getChave())
                .compact();
    }
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }
    public String extrairRole(String token) {
        return extrairClaims(token).get("role", String.class);
    }
    public boolean tokenValido(String token) {
        try {
            Claims claims = extrairClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getChave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}