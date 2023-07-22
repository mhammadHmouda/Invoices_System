package com.harri.training1.models.dto;

import com.harri.training1.models.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private RoleName role;
}
