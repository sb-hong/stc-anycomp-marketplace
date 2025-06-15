package com.stctest.anycompmarketplace.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String secret = "94de4b1000c00b2a3aec573a28e19e7b5616a8730fdce695425b53274c005b91986f50750ab99ec80f551c10b1c56cbb4844d8344cc92fd27fc6da13f6321df20843d41a89153e1356bb60b5036b996f730b87f763f9541ca196f310cd76d2e431ee4ef1d66b13f189cc0a0e323e3efb82806420bbdf898498ea2b3c93ba2f2892080638f86e5e97b14555ef68d53755815c67e73eb4553ef007227434564f4691cb0e7cc391b1135ffcb5494d613c3cbac3ea1a42374df31e07b1496a40f56b5da3e1095174135b74a2345e4c9a3da5ab44e97902aeadaa8ec5a63faf4224ba22ffff5448900129fa360096973cef29f9ae7489fa5d3d29906acc4f9f1ade47"; // Secure this key properly
    private final long expirationMs = 1800000; // 30 mins

    public String generateToken(String username) { // Use email as username
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}