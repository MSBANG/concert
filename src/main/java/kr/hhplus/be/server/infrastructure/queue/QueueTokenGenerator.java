package kr.hhplus.be.server.infrastructure.queue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.hhplus.be.server.config.jwt.JwtConfig;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class QueueTokenGenerator {

    private final Key secretKey;
    private final ObjectMapper objectMapper;

    public QueueTokenGenerator(JwtConfig jwtConfig, ObjectMapper objectMapper) {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        this.objectMapper = objectMapper;
    }

    public String encode(Object data) {
        Map<String, Object> claims = objectMapper.convertValue(data, new TypeReference<Map<String, Object>>() {});
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T decode(String token, Class<T> targetType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return this.objectMapper.convertValue(claims, targetType);
    }
}
