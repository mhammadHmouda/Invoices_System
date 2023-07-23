package com.harri.training1.models.dto;

import com.harri.training1.annotations.IsValidPassword;
import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    @IsValidPassword
    private String password;
}
