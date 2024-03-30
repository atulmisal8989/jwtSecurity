package com.example.config;

import com.example.constant.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtTokenGeneratorFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.info("Inside doFilter");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes());

            String jwt = Jwts.builder()
                    .setIssuer("Atul Misal")
                    .setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities())) // will accept comma separated value for multiple authorities
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + 8 * 60 * 60 * 1000))
                    .signWith(key).compact();

            response.setHeader(SecurityConstants.JWT_HEADER, jwt);

        }

        filterChain.doFilter(request, response);
    }


    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {

        Set<String> authoritiesSet = new HashSet<>();

        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);


    }

    //this make sure that this filter will execute only for first time when client call the api /signIn at first time

    // If it return true means to avoid filtering of the given request.
    // If it return false means to do filtering of the given request.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return !request.getServletPath().equals("/students/signIn");
    }
}
