package com.harri.training1.services;

import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.entities.User;
import com.harri.training1.models.dto.RegisterDto;
import com.harri.training1.repositories.UserRepository;
import com.harri.training1.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AutoMapper<User, RegisterDto> autoMapper;
    private final SecurityConfig securityConfig;

    public void register(RegisterDto registerDto) {
        User user = autoMapper.toModel(registerDto, User.class);
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
}
