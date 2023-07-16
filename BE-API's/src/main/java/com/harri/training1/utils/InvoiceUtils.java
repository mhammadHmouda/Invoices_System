package com.harri.training1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harri.training1.models.entities.File;
import com.harri.training1.models.entities.Invoice;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvoiceUtils {
    public final static String SAVE_PATH =  "src\\main\\resources\\files\\";

    public static void saveFile(MultipartFile file) {
        Path filePath = Path.of(SAVE_PATH + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error on save file: " + file.getOriginalFilename());
        }
    }

    public static List<File> extractFilesDetails(List<MultipartFile> files) {
        List<File> invoiceFiles = new ArrayList<>();

        files.forEach(file -> {
            InvoiceUtils.saveFile(file);
            File newFile = new File();
            newFile.setSize(file.getSize());
            newFile.setName(Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0]);
            newFile.setType(file.getOriginalFilename().split("\\.")[1]);
            newFile.setPath(InvoiceUtils.SAVE_PATH + file.getOriginalFilename());
            invoiceFiles.add(newFile);
        });

        return invoiceFiles;
    }

    public static Invoice convertJsonStringToObject(String invoiceJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(invoiceJson, Invoice.class);
    }
}
