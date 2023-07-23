package com.harri.training1.controllers;

import com.harri.training1.models.dto.LoginDto;
import com.harri.training1.models.dto.RegisterDto;
import com.harri.training1.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AuthController class is a REST controller that handles authentication-related requests.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    private final AuthService authService;

    /**
     * Handles the login request.
     *
     * @param loginDto  the LoginDto object containing login credentials
     * @param response  the HttpServletResponse object to set the response
     * @return ResponseEntity containing the JWT token if login is successful
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String jwt = authService.login(loginDto, response);
        LOGGER.info("log in user with name: " + loginDto.getUsername());
        return ResponseEntity.ok().body(jwt);
    }

    /**
     * Handles the registration request.
     *
     * @param dto  the RegisterDto object containing user registration details
     * @return ResponseEntity with a success message if user registration is successful
     */
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        authService.register(dto);
        LOGGER.info("register new user with name: " + dto.getUsername());
        return ResponseEntity.ok().body("User created successfully!");
    }
}