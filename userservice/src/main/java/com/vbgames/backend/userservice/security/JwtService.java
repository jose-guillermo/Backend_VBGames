package com.vbgames.backend.userservice.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Value("${jwt.access.private}")
    private String privateAccess;

    @Value("${jwt.refresh.private}")
    private String privateRefresh;

    private PrivateKey PRIVATE_ACCESS_KEY;
    private PrivateKey PRIVATE_REFRESH_KEY;
    private final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private final String COOKIE_ACCESS_TOKEN = "accessToken";
    public static final String CONTENT_TYPE = "application/json";

    @PostConstruct
    public void init(){
        PRIVATE_ACCESS_KEY = getPrivateKey(privateAccess);
        PRIVATE_REFRESH_KEY = getPrivateKey(privateRefresh);
    }
    
    // Decodifico las claves privadas y genero las llaves
    private PrivateKey getPrivateKey(String base64Key) {
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
            .expiration(new Date(System.currentTimeMillis() + (3600000 * 5)))
            // .expiration(new Date(System.currentTimeMillis() + 1))
            .issuedAt(new Date())
            .signWith(PRIVATE_ACCESS_KEY, Jwts.SIG.RS256)
            .compact();
    }

    public String createRefreshToken(String userId) {
        return Jwts.builder()
            .subject(userId)
            .expiration(new Date(System.currentTimeMillis() + (3600000 * 24 * 7)))
            .issuedAt(new Date())
            .signWith(PRIVATE_REFRESH_KEY, Jwts.SIG.RS256)
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
}
