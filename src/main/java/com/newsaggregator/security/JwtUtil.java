package com.newsaggregator.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration-ms}")
	private long jwtExpirationMs;

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(userDetails.getUsername());
	}

	public String generateToken(String username) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiry)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean isTokenExpired(String token) {
		Date expiration = parseClaims(token).getExpiration();
		return expiration.before(new Date());
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}
