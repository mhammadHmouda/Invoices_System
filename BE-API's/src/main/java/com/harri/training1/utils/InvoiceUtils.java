package com.harri.training1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harri.training1.models.entities.File;
import com.harri.training1.models.entities.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class contains some function useful to the invoice operations
 */
@Service
public class InvoiceUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceUtils.class);
    public final static String SAVE_PATH =  "src\\main\\resources\\files\\";

    /**
     * This function take a file and save this file in the specified path
     *
     * @param file this is a file I want to save
     */
    public void saveFile(MultipartFile file) {
        Path filePath = Path.of(SAVE_PATH + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Save new file with name: " + file.getOriginalFilename() + ", in the path: (" + filePath + ")");
        } catch (IOException e) {
            LOGGER.error("Something went wrong on save the file," +
                    " Check the path you want to save files on it!");
            throw new RuntimeException("Error on save file: " + file.getOriginalFilename());
        }
    }

    /**
     * This function to extract useful details from multipart file
     *
     * @param files I want to save
     * @return the list of file object represent the files info
     */
    public List<File> extractFilesDetails(List<MultipartFile> files) {
        List<File> invoiceFiles = new ArrayList<>();

        files.forEach(file -> {
            saveFile(file);
            File newFile = new File();
            newFile.setSize(file.getSize());
            newFile.setName(Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0]);
            newFile.setType(file.getOriginalFilename().split("\\.")[1]);
            newFile.setPath(InvoiceUtils.SAVE_PATH + file.getOriginalFilename());
            invoiceFiles.add(newFile);
        });

        LOGGER.info("Extract invoice details from files selected.");
        return invoiceFiles;
    }

    /**
     * This function to convert invoiceJson string to the Invoice object
     *
     * @param invoiceJson string
     * @return invoice object
     */
    public Invoice convertJsonStringToObject(String invoiceJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Invoice invoice = objectMapper.readValue(invoiceJson, Invoice.class);
        LOGGER.info("Convert invoice json string to the invoice object");

        return invoice;
    }
}
