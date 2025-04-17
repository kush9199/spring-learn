//package com.studenterp.jwtauth.service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JWTService {
//    private final String SECRET_KEY = "thisKeyShouldBeSecretAndReallyBigInSize";
//    private final Long EXPIRATION_TIME = 100 * 60 * 60L;
//
//    private SecretKey getSecretKey() {
//        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//    }
//
//    private String generateTokenWithClaims(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .claims(claims)
//                .subject(subject)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(getSecretKey())
//                .compact();
//    }
//
//    private Claims getClaimsFromToken(String token) {
//        return Jwts
//                .parser()
//                .verifyWith(getSecretKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//
//    }
//
//    private String extractUsername(String token, Function<Claims, String> usernameClaim) {
//        final Claims claims = getClaimsFromToken(token);
//        return usernameClaim.apply(claims);
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return getClaimsFromToken(token).getExpiration().before(new Date());
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("authorities", userDetails.getAuthorities());
//        return generateTokenWithClaims(claims, userDetails.getUsername());
//    }
//
//    public String getUsernameFromToken(String token) {
//        return extractUsername(token, (claims) -> claims.getSubject());
//    }
//
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//}
