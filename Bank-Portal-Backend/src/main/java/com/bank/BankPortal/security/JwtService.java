package com.bank.BankPortal.security;

import com.bank.BankPortal.entity.User;
import com.bank.BankPortal.exception.UnauthorizedAccessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final long accessExpirationMs = 1000 * 60 * 15;  // 15 minutes
    private static final long refreshExpirationMs = 1000L * 60 * 60 * 24 * 7;  // 7 days

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // === Extract ===
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println("Extracted claims: " + claims);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        System.out.println("Extracting claims from token: " + token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | SignatureException e) {
            throw new UnauthorizedAccessException("" + e);
        } catch (io.jsonwebtoken.JwtException e) { // catches other parsing exceptions
            throw new UnauthorizedAccessException("" + e);
        }
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // === Validation ===
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    // === Generation ===
    public String generateToken(User user, boolean isRefresh) {
        long expiration = isRefresh ? refreshExpirationMs : accessExpirationMs;

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
