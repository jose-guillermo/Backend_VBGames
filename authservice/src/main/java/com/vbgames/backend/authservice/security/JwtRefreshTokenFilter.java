package com.vbgames.backend.authservice.security;

import static com.vbgames.backend.authservice.services.JwtService.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseCookie;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbgames.backend.authservice.dtos.UserResponse;
import com.vbgames.backend.authservice.services.JwtService;
import com.vbgames.backend.authservice.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if(!"/refresh".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        String userId = request.getHeader("X-User-Id");

        UserResponse user = userService.getUser(UUID.fromString(userId));
        List<String> roles = user.getRoles();

        String accessToken = jwtService.createAccessToken(userId, roles);
        String refreshToken = jwtService.createRefreshToken(userId);

        ResponseCookie accessJwtCookie = jwtService.createAccessCookie(accessToken);
        ResponseCookie refreshJwtCookie = jwtService.createRefreshCookie(refreshToken);

        response.addHeader("Set-Cookie", accessJwtCookie.toString());
        response.addHeader("Set-Cookie", refreshJwtCookie.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(CONTENT_TYPE);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Se ha renovado el access token con éxito");
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }    
}
