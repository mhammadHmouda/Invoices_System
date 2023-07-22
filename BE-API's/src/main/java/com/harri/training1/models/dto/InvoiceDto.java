package com.harri.training1.models.dto;

import com.harri.training1.models.entities.File;
import com.harri.training1.models.entities.Item;
import com.harri.training1.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
    private Long id;

    private Date createdAt;

    private float totalPrice;

    private User user;

    private List<Item> items;

    private List<File> files;
}
