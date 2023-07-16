package com.harri.training1.controllers;

import com.harri.training1.models.entities.Invoice;
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
        invoiceService.addInvoice(invoice, files);
        return ResponseEntity.ok("Invoice added successfully!");
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber){
        Page<Invoice> invoices = invoiceService.findAll(pageNumber);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Invoice invoice = invoiceService.findById(id);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping
    public ResponseEntity<?> updateInvoice(@RequestBody Invoice invoice){
        invoiceService.updateInvoice(invoice);
        return ResponseEntity.ok("Invoice updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        invoiceService.deleteById(id);
        return ResponseEntity.ok("Invoice deleted successfully!");
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable Long id){
        List<Invoice> invoices = invoiceService.findByUserId(id);
        return ResponseEntity.ok(invoices);
    }

}
