package com.vbgames.backend.authservice.services;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.vbgames.backend.authservice.exceptions.InvalidCredentialsException;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.access.private}")
    private String privateAccess;

    @Value("${jwt.refresh.secret}")
    private String secretRefresh;

    @Value("${jwt.verify-email.secret}")
    private String secretVerifyEmail;

    private PrivateKey PRIVATE_ACCESS_KEY;
    private SecretKey REFRESH_SECRET_KEY;
    private SecretKey VERIFY_EMAIL_SECRET_KEY;
    public static final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private final String COOKIE_ACCESS_TOKEN = "accessToken";
    public static final String CONTENT_TYPE = "application/json";

    @PostConstruct
    public void init(){
        PRIVATE_ACCESS_KEY = getKey(privateAccess);
        REFRESH_SECRET_KEY = Keys.hmacShaKeyFor(secretRefresh.getBytes());
        VERIFY_EMAIL_SECRET_KEY = Keys.hmacShaKeyFor(secretVerifyEmail.getBytes());
    }

    // Decodifico las claves privadas y genero las llaves
    private PrivateKey getKey(String base64Key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    public String createAccessToken(String userId, List<String> roles) {
        Claims claims = Jwts.claims()
            .add("authorities", roles)
            .build();

        return Jwts.builder()
            .subject(userId)
            .claims(claims)
            .expiration(new Date(Instant.now().plus(5, ChronoUnit.HOURS).toEpochMilli()))
            .issuedAt(new Date())
            .signWith(PRIVATE_ACCESS_KEY, Jwts.SIG.RS256)
            .compact();
    }

    public String createRefreshToken(String userId) {
        return Jwts.builder()
            .subject(userId)
            .expiration(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
            .issuedAt(new Date())
            .signWith(REFRESH_SECRET_KEY)
            .compact();
    }

    public ResponseCookie createAccessCookie(String accessToken) {
        return ResponseCookie.from(COOKIE_ACCESS_TOKEN, accessToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(3600 * 5)
            .sameSite("None")
            // .domain("localhost")
            .build();
    }

    public ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from(COOKIE_REFRESH_TOKEN, refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(3600 * 24 * 7)
            .sameSite("None")
            // .domain("localhost")
            .build();
    }

    public String createEmailToken(String email) {
        return Jwts.builder()
            .subject(email)
            .expiration(new Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli()))
            .issuedAt(new Date())
            .signWith(VERIFY_EMAIL_SECRET_KEY)
            .compact();
    }

    public Claims verifyRefreshToken(String token) {
        try {
            return Jwts.parser().verifyWith(REFRESH_SECRET_KEY).build().parseSignedClaims(token).getPayload();   
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }
    }

    public Claims verifyEmailToken(String token) {
        return Jwts.parser().verifyWith(VERIFY_EMAIL_SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }
}
