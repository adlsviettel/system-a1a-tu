package com.allianceoneapparel.config;

import com.allianceoneapparel.AuthAccountURL;
import com.allianceoneapparel.token.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        try {
            if (shouldBypassAuthentication(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (isTokenInvalid(authHeader)) {
                filterChain.doFilter(request, response);
                return;
            }

            processAuthentication(authHeader, request);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException jwtException) {
            //Token Expired
        }
    }

    private boolean shouldBypassAuthentication(HttpServletRequest request) {
        return request.getServletPath().contains(AuthAccountURL.AUTH);
    }

    private boolean isTokenInvalid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private void processAuthentication(String authHeader, HttpServletRequest request) {
        final String jwt = authHeader.substring(7);
        String username = jwtService.extractUsername(jwt);
        if (StringUtils.hasText(jwt) && (username != null)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                authenticateUser(userDetails, request);
            }
        }
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
