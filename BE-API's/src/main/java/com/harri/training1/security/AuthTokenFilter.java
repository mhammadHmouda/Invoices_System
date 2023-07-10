package com.harri.training1.security;

import com.harri.training1.models.entities.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);
            jwt = jwt.split("\\|")[0];
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                Long userId = jwtUtils.getIdFromJwtToken(jwt);
                String userRole = jwtUtils.getRoleFromJwtToken(jwt);

                LOGGER.info("doFilterInternal :: got username = " + username + " with id = " + userId + " and role = " + userRole + " from jwt.");

                UserDetailsImpl userDetails = jwtUtils.loadUserByUsername(username);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRole));

                userDetails.setAuthorities(authorities);

                if (userDetails.getRole().equalsIgnoreCase(userRole)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    LOGGER.warn("doFilterInternal :: Invalid user role for the requested resource");
                }
            }

        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
