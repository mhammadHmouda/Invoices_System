package com.harri.training1.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.models.entities.User;
import com.harri.training1.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<?> addInvoice(@RequestParam("files") List<MultipartFile> files, @RequestParam("invoice") String invoice){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Invoice inv = objectMapper.readValue(invoice, Invoice.class);

            invoiceService.addInvoice(inv, files);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Invoice added successfully!");
    }
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Invoice> invoices = invoiceService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.ok(invoice);
    }
}
