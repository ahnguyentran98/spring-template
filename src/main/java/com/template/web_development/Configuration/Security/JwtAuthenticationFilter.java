package com.template.web_development.Configuration.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final TokenService tokenService;

    private final UserSecurityInfoService userSecurityInfoService;

    public JwtAuthenticationFilter(TokenService tokenService, UserSecurityInfoService userSecurityInfoService) {
        this.tokenService = tokenService;
        this.userSecurityInfoService = userSecurityInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("X-Auth-Token");
        try {
            if (token != null && tokenService.validateToken(token)) {
                String userAccountName = tokenService.getUserAccountName(token);

                UserDetails userDetails = userSecurityInfoService.loadUserByUsername(userAccountName);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT Token is expired", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT Token is expired");
            return;
        } catch (JwtException e) {
            LOGGER.error("JWT processing error", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT processing error: " + e.getMessage());
            return;
        } catch (Exception e) {
            LOGGER.error("JwtAuthenticationFilter exception", e);
            return;
        }
        chain.doFilter(request, response);
    }
}

