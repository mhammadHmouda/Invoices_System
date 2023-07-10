package com.harri.training1.security;

import com.harri.training1.models.entities.RefreshToken;
import com.harri.training1.models.entities.User;
import com.harri.training1.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${harri.app.jwtSecret}")
    private String jwtSecret;

    @Value("${harri.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${harri.app.jwtCookieName}")
    private String jwtCookie;

    @Autowired
    private UserRepository repository;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            LOGGER.info("getJwtFromCookies :: Got token from cookies : " + cookie.getValue());
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal, RefreshToken refreshToken) {
        String jwt = generateTokenFromUserDetails(userPrincipal);
        LOGGER.info("generateJwtCookie :: Token generated : " + jwt);

        String refreshTokenValue = refreshToken.getToken();

        String cookieValue = jwt + "|" + refreshTokenValue;

        ResponseCookie cookie = ResponseCookie.from(jwtCookie, cookieValue)
                .path("/api").maxAge(jwtExpirationMs / 1000)
                .httpOnly(true).build();

        LOGGER.info("generateJwtCookie :: Generate token cookie : " + cookie);
        return cookie;
    }

    public String generateTokenFromUserDetails(UserDetailsImpl userDetails) {
        LOGGER.debug("generateTokenFromUserDetails :: Generating token from user details: " + userDetails);
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("id", userDetails.getId().intValue());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole());

        byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, "").path("/api").build();
        LOGGER.info("getCleanJwtCookie :: Toke removed from cookie : " + cookie);
        return cookie;
    }

    public String getUserNameFromJwtToken(String token) {
        LOGGER.debug("getUserNameFromJwtToken :: Getting username from token..");
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().getSubject();
    }
    public Long getIdFromJwtToken(String token) {
        LOGGER.debug("getIdFromJwtToken :: Getting id from token");
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("id", Long.class);
    }
    public String getRoleFromJwtToken(String token) {
        LOGGER.debug("getRoleFromJwtToken :: Getting role from token");
        return Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(token.split("\\|")[0]).getBody().get("role", String.class);
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtSecret)).build().parseClaimsJws(authToken);
            LOGGER.info("validateJwtToken :: Token validated");
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> users = repository.findByUsername(username);

        if (users.isEmpty())
            return null;

        User user = users.get();

        return UserDetailsImpl.build(user);
    }
}
