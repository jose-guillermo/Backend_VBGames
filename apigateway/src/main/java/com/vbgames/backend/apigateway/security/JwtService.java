package com.vbgames.backend.apigateway.security;

import java.net.HttpCookie;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


public class JwtService {

    @Value("${jwt.access.secret}")
    private String secretAccess;

     @Value("${jwt.refresh.secret}")
    private String secretRefresh;

    private SecretKey SECRET_ACCESS_KEY;
    private SecretKey SECRET_REFRESH_KEY;
    private String COOKIE_REFRESH_TOKEN = "refreshToken";
    private String COOKIE_ACCESS_TOKEN = "accessToken";
    public static final String CONTENT_TYPE = "application/json";

    @PostConstruct
    public void init(){
        SECRET_ACCESS_KEY = Keys.hmacShaKeyFor(secretAccess.getBytes());
        SECRET_REFRESH_KEY = Keys.hmacShaKeyFor(secretRefresh.getBytes());
    }

    public String createAccessToken(String username, List<String> roles) {
        Claims claims = Jwts.claims()
            .add("authorities", roles)
            .add("username", username)
            .build();

        return Jwts.builder()
            .subject(username)
            .claims(claims)
            .expiration(new Date(System.currentTimeMillis() + (3600000 * 5)))
            .issuedAt(new Date())
            .signWith(SECRET_ACCESS_KEY)
            .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
            .subject(username)
            .expiration(new Date(System.currentTimeMillis() + (3600000 * 24 * 7)))
            .issuedAt(new Date())
            .signWith(SECRET_REFRESH_KEY)
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

    public String getAccessToken(HttpCookie[] cookies) {
        String token = null;
        if(cookies != null) {
            for (var cookie : cookies){
                if(COOKIE_ACCESS_TOKEN.equals(cookie.getName())){
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    public String getRefreshToken(HttpCookie[] cookies) {
        String token = null;
        if(cookies != null) {
            for (var cookie : cookies){
                if(COOKIE_REFRESH_TOKEN.equals(cookie.getName())){
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    public Claims verifyAccessToken(String token) {
        return Jwts.parser().verifyWith(SECRET_ACCESS_KEY).build().parseSignedClaims(token).getPayload();
    }

    public Claims verifyRefreshToken(String token) throws IllegalArgumentException{
        return Jwts.parser().verifyWith(SECRET_REFRESH_KEY).build().parseSignedClaims(token).getPayload();
    }
}
