package com.vbgames.backend.apigateway.security;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Value("${jwt.access.public}")
    private String publicAccess;

    @Value("${jwt.refresh.public}")
    private String publicRefresh;

    private PublicKey PUBLIC_ACCESS_KEY;
    private PublicKey PUBLIC_REFRESH_KEY;
    private final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private final String COOKIE_ACCESS_TOKEN = "accessToken";
    public static final String CONTENT_TYPE = "application/json";

    @PostConstruct
    public void init(){
        PUBLIC_ACCESS_KEY = getPublicKey(publicAccess);
        PUBLIC_REFRESH_KEY = getPublicKey(publicRefresh);
    }

    private PublicKey getPublicKey(String base64Key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading private key", e);
        }
    }

    public String getAccessToken(MultiValueMap<String, HttpCookie> cookies) {
        if(cookies == null) return null;
        HttpCookie cookie = cookies.getFirst(COOKIE_ACCESS_TOKEN);

        return cookie != null ? cookie.getValue() : null;
    }

    public String getRefreshToken(MultiValueMap<String, HttpCookie> cookies) {
        if(cookies == null) return null;
        HttpCookie cookie = cookies.getFirst(COOKIE_REFRESH_TOKEN);

        return cookie != null ? cookie.getValue() : null;
    }

    public Claims verifyAccessToken(String token) {
        return Jwts.parser().verifyWith(PUBLIC_ACCESS_KEY).build().parseSignedClaims(token).getPayload();
    }

    public Claims verifyRefreshToken(String token) throws IllegalArgumentException{
        return Jwts.parser().verifyWith(PUBLIC_REFRESH_KEY).build().parseSignedClaims(token).getPayload();
    }
}
