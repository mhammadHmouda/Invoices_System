package com.harri.training1.controllers;

import com.harri.training1.models.dto.LoginDto;
import com.harri.training1.models.dto.RegisterDto;
import com.harri.training1.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String jwt = authService.login(loginDto, response);
        return ResponseEntity.ok().body(jwt);
    }


    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto){
        authService.register(dto);
        return ResponseEntity.ok().body("User created successfully!");
    }

}
