package com.nix.ecommerceapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nix.ecommerceapi.exception.AuthFailureException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.access-token-expiration-ms}")
    private int accessTokenExpirationMs;
    @Value("${jwt.refresh-token-expiration-ms}")
    private int refreshTokenExpirationMs;
    @Value("${jwt.secretKey}")
    private String secretKey;

    public Token generateTokenPair(JwtPayload payload) {
        String accessToken = this.generateToken(payload, accessTokenExpirationMs);
        String refreshToken = this.generateToken(payload, refreshTokenExpirationMs);
        return new Token(accessToken, refreshToken);
    }

    public JwtPayload getPayLoadIfTokenValidated(String token) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            Object payload = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().get("payload");
            return mapper.convertValue(payload, JwtPayload.class);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new AuthFailureException("INVALID_CREDENTIALS");
        }
    }

    public String generateToken(JwtPayload payload, int expirationTimeMs) {
        Date now = new Date();
        Map<String, JwtPayload> claims = new HashMap<>();
        claims.put("payload", payload);
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(payload.getUserId().toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTimeMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
