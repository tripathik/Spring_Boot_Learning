package com.intelliguru.simplora.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private JwtUtil(){}

    private static  final String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public static Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // claims is nothing but some payload/additional data. It is not mandatory.
        return createToken(claims, username);
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject) // Subject is nothing but uniqueness and in this case it would be UserName
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis())) // Time at which JWT token is being created.
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 minutes expiration time
                .signWith(getSigningKey())
                .compact();
    }

    public static Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
