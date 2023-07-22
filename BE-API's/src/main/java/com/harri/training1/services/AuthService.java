package com.harri.training1.services;

import com.harri.training1.exceptions.LoginFailedException;
import com.harri.training1.exceptions.UserFoundException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.dto.LoginDto;
import com.harri.training1.models.entities.User;
import com.harri.training1.models.dto.RegisterDto;
import com.harri.training1.models.enums.RoleName;
import com.harri.training1.repositories.UserRepository;
import com.harri.training1.security.JwtUtils;
import com.harri.training1.security.SecurityConfig;
import com.harri.training1.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The AuthService class provides authentication and registration-related services.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final AutoMapper<User, RegisterDto> autoMapper;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginDto   the LoginDto object containing login credentials
     * @param response   the HttpServletResponse object to set the response
     * @return the generated JWT token
     */
    public String login(LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateTokenFromUserDetails(userDetails);

            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

            LOGGER.info("Check user credentials and set user authentication.");
            return jwt;
        } catch (Exception e) {
            LOGGER.error("Something went wrong when login user!");
            throw new LoginFailedException(e.getMessage());
        }
    }

    /**
     * Registers a new user and save this user in database.
     *
     * @param registerDto   the RegisterDto object containing user registration details
     */
    public void register(RegisterDto registerDto) {
        Optional<User> test = userRepository.findByUsername(registerDto.getUsername());
        if (test.isPresent()) {
            LOGGER.error("User with name: (" + registerDto.getUsername() + ") Not exist!");
            throw new UserFoundException("Please try entering another username!");
        }

        User user = autoMapper.toModel(registerDto, User.class);
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        user.setRole(RoleName.USER);

        userRepository.save(user);
        LOGGER.info("Register new user in the system with name: " + registerDto.getUsername());
    }
}
