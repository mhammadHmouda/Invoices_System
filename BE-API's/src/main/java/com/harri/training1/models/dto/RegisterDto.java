package com.harri.training1.models.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
}
