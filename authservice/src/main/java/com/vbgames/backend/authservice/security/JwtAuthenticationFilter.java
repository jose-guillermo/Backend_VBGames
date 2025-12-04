package com.vbgames.backend.authservice.security;

import static com.vbgames.backend.authservice.services.JwtService.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbgames.backend.common.exceptions.ApiError;
import com.vbgames.backend.authservice.entities.User;
import com.vbgames.backend.authservice.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        User user = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Formato de usuario incorrecto", e);
        }

        if(user == null || user.getEmail() == null || user.getPassword() == null)
            throw new AuthenticationServiceException("Formato de usuario incorrecto");
        
        String email = user.getEmail().toLowerCase();
        String password = user.getPassword();


        if(email.equals("system@internal.local"))
            throw new BadCredentialsException("Este usuario no puede iniciar sesión");
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authResult.getPrincipal();
        String userId = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        List<String> roleNames = roles.stream().map(GrantedAuthority::getAuthority).toList();
        
        String accessToken = jwtService.createAccessToken(userId, roleNames);
        String refreshToken = jwtService.createRefreshToken(userId);

        ResponseCookie accessJwtCookie = jwtService.createAccessCookie(accessToken);
        ResponseCookie refreshJwtCookie = jwtService.createRefreshCookie(refreshToken);
        
        response.addHeader("Set-Cookie", accessJwtCookie.toString());
        response.addHeader("Set-Cookie", refreshJwtCookie.toString());
        // response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, String> body = new HashMap<>();
        // body.put("token", token);
        body.put("userId", userId);
        body.put("message", String.format("Hola %s has iniciado sesión con exito!", userId));
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

        ApiError error = new ApiError("Error de autenticación", statusCode, request);

        response.setStatus(statusCode.value());
        response.setContentType(CONTENT_TYPE);

        objectMapper.writeValue(response.getWriter(), error);
    }

}
