package com.harri.training1.controllers;

import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.services.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * The InvoiceController class is a REST controller that handles invoice-related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    private final InvoiceService invoiceService;

    /**
     * Handles the request to add an invoice.
     *
     * @param files        the list of MultipartFile objects representing uploaded files
     * @param invoiceDto   the String representation of the invoice details
     * @return ResponseEntity with a success message if the invoice is added successfully
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'USER')")
    public ResponseEntity<?> addInvoice(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("invoice") String invoiceDto) {

        invoiceService.addInvoice(invoiceDto, files);
        LOGGER.info("Add new invoice.");
        return ResponseEntity.ok("Invoice added successfully!");
    }

    /**
     * Handles the request to retrieve a paginated list of invoices.
     *
     * @param pageNumber   the page number to retrieve (default: 0)
     * @return ResponseEntity containing a Page object with the list of invoices
     */
    @GetMapping("/paging")
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'AUDITOR')")
    public ResponseEntity<?> findAllPaging(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        Page<InvoiceDto> invoices = invoiceService.findAllPaging(pageNumber);
        LOGGER.info("Get 10 invoices for page number: " + pageNumber);
        return ResponseEntity.ok(invoices);
    }

    /**
     * Handles the request to retrieve a list of all invoices.
     *
     * @return ResponseEntity containing a list of all invoices
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'AUDITOR')")
    public ResponseEntity<?> findAll() {
        List<InvoiceDto> invoices = invoiceService.findAll();
        LOGGER.info("Get all invoices.");
        return ResponseEntity.ok(invoices);
    }

    /**
     * Handles the request to retrieve an invoice by ID.
     *
     * @param id   the ID of the invoice to retrieve
     * @return ResponseEntity containing the retrieved invoice
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        InvoiceDto invoice = invoiceService.findById(id);
        LOGGER.info("Get invoice with id = " + id);
        return ResponseEntity.ok(invoice);
    }

    /**
     * Handles the request to update an invoice.
     *
     * @param invoice   the Invoice object representing the updated invoice
     * @return ResponseEntity with a success message if the invoice is updated successfully
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'USER')")
    public ResponseEntity<?> updateInvoice(@Valid @RequestBody InvoiceDto invoice) {
        invoiceService.update(invoice);
        LOGGER.info("update invoice with id = " + invoice.getId());
        return ResponseEntity.ok("Invoice updated successfully!");
    }

    /**
     * Handles the request to delete an invoice by ID.
     *
     * @param id   the ID of the invoice to delete
     * @return ResponseEntity with a success message if the invoice is deleted successfully
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPERUSER', 'USER')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        invoiceService.deleteById(id);
        LOGGER.info("Delete invoice with id = " + id);
        return ResponseEntity.ok("Invoice deleted successfully!");
    }

    /**
     * Handles the request to retrieve a list of invoices by user ID.
     *
     * @param id   the ID of the user
     * @return ResponseEntity containing a list of invoices associated with the user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> findByUserId(@PathVariable Long id) {
        List<InvoiceDto> invoices = invoiceService.findByUserId(id);
        LOGGER.info("Get invoice by user id = " + id);
        return ResponseEntity.ok(invoices);
    }

}