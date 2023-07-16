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
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AutoMapper<User, RegisterDto> autoMapper;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public String login(LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateTokenFromUserDetails(userDetails);

            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

            return jwt;
        }
        catch (Exception e){
            throw new LoginFailedException(e.getMessage());
        }

    }

    public void register(RegisterDto registerDto) {
        Optional<User> test = userRepository.findByUsername(registerDto.getUsername());
        if (test.isPresent())
            throw new UserFoundException("Please try enter another username!");

        User user = autoMapper.toModel(registerDto, User.class);
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        user.setRole(RoleName.AUDITOR);

        userRepository.save(user);
    }
}
