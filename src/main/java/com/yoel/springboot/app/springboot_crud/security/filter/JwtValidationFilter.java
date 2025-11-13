package com.yoel.springboot.app.springboot_crud.security.filter;

import static com.yoel.springboot.app.springboot_crud.security.TokenJwtConfig.CONTENT_TYPE;
import static com.yoel.springboot.app.springboot_crud.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.yoel.springboot.app.springboot_crud.security.TokenJwtConfig.PREFIX_TOKEN;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoel.springboot.app.springboot_crud.security.SimpleGrantedAuthorityJsonCreator;
import com.yoel.springboot.app.springboot_crud.security.TokenJwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        String header = request.getHeader(HEADER_AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ") || header.equals("Bearer null") || header.isBlank() || header.equals("Bearer ")) {
            chain.doFilter(request, response);
        }

        String token = null;

        if (header != null) {
            token = header.replace(PREFIX_TOKEN, "");
        
        try {
            Claims claims = Jwts.parser()
                .verifyWith(TokenJwtConfig.SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            String username = claims.getSubject();
            Object authoritiesClaims = claims.get("authorities");
            
            if  (authoritiesClaims == null || authoritiesClaims.toString().isBlank())  {
                throw new JwtException("Token is valid but authorities claim is missing or empty.");
            }
            
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
            .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
            .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)); 
                        
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        } catch (JwtException e) {
        
            Map<String, String> body = Map.of(
                "message", "Invalid or expired token",
                "error", e.getMessage()
            );

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }
        }

    }

}
