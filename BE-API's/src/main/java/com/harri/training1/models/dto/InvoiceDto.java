package com.harri.training1.models.dto;

import com.harri.training1.models.entities.File;
import com.harri.training1.models.entities.InvoiceItem;
import com.harri.training1.models.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private User user;

    private List<InvoiceItem> invoiceItems;
    private List<File> files;
}
