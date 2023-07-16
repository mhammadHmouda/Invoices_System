package com.harri.training1.models.dto;

import com.harri.training1.models.enums.RoleName;
import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;
}
