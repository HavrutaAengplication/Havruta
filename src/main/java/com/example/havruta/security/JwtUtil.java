package com.example.havruta.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String SECRET_KEY = "secretaskdjflkjqwqwerqwelkfniwreupjfkajkdfsajkfawefknqlwnefqd";

    public String generateToken(Integer userID) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userID);
    }

    private String createToken(Map<String, Object> claims, Integer subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(subject))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, Integer userId) {
        final Integer tokenUserID = extractUserId(token);
        return (tokenUserID.equals(userId) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Integer extractUserId(String token) {
        return Integer.valueOf(extractClaim(token, Claims::getSubject));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token.substring(7)).getBody();
    }
}

