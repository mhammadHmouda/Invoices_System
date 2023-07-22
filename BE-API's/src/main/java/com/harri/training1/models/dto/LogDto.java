package com.harri.training1.models.dto;

import com.harri.training1.models.entities.User;
import com.harri.training1.models.enums.Action;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {

    private Long id;

    private Date createdAt;

    private String description;

    @Enumerated(EnumType.STRING)
    private Action action;

    private User user;
}
