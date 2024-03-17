package com.mys.CommerceHub.conf.security;

import com.mys.CommerceHub.dao.auth.UserDao;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDao userRepo;
    private final CommerceHubUserDetailService commerceHubUserDetailService;

    public JwtTokenFilter(JwtTokenService jwtTokenService, UserDao userRepo, CommerceHubUserDetailService commerceHubUserDetailService) {
        this.jwtTokenService = jwtTokenService;
        this.userRepo = userRepo;
        this.commerceHubUserDetailService = commerceHubUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        final String username = jwtTokenService.extractUsername(token);
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }


        // Get user identity and set it on the spring security context
        UserDetails userDetails = commerceHubUserDetailService
                .loadUserByUsername(username);

        if(userDetails == null || !jwtTokenService.isTokenValid(token,userDetails)){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
