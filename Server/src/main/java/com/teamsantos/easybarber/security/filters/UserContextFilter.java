package com.teamsantos.easybarber.security.filters;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.teamsantos.easybarber.exceptions.InvalidTokenException;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.security.utils.UserPrincipal;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserContextFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    public UserContextFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);
            if (token != null) {
                Claims claims = jwtUtils.validateToken(token);
                if (claims != null) {
                    UserPrincipal userDetails = jwtUtils.parseToken(claims);
                    UserContext.setCurrentUser(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getUserDetails().getAuthorities()));
                    if (jwtUtils.isTokenExpiringSoon(claims)) {
                        response.setHeader("Authorization", "Bearer " + jwtUtils.generateToken(userDetails));
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token\", \"message\": \"" + e.getMessage() + "\"}");
            response.getWriter().flush();
        } finally {
            UserContext.clear();
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (null != bearerToken && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
