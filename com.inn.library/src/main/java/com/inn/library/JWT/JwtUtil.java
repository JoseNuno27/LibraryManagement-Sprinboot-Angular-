package com.inn.library.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Service
public class JwtUtil {

    private final String secret = "tokenJson";

        public String extractUsername(String token) {
            return extractClaims(token, Claims::getSubject);
        }

        public Date extractExpiration(String token) {
            return extractClaims(token, Claims::getExpiration);
        }

        public Boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(String username,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims,username);
    }


    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret).compact();
    }


    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims (String token) {
       return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }



    public boolean isTokenValid(String jwt) {
        Claims claims = extractAllClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
