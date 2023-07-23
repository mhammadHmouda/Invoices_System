package com.harri.training1.services;

import com.harri.training1.exceptions.InvoiceNotAddedException;
import com.harri.training1.exceptions.InvoiceNotExistException;
import com.harri.training1.exceptions.NoInvoicesForThisPageException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.dto.InvoiceDto;
import com.harri.training1.models.entities.File;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.repositories.InvoiceRepository;
import com.harri.training1.utils.InvoiceUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

/**
 * The InvoiceService class provides invoice-related services.
 */
@Service
@RequiredArgsConstructor
public class InvoiceService implements BaseService<InvoiceDto, Long>{
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceService.class);
    private final InvoiceRepository invoiceRepository;
    private final LogService logService;
    private final InvoiceUtils invoiceUtils;
    private final AutoMapper<Invoice, InvoiceDto> mapper;

    /**
     * Adds an invoice with the provided JSON representation and associated files.
     * And create new add log and store the important info about this action.
     *
     * @param invoiceJson   the invoice represent in string in JSON format.
     * @param files         the list of MultipartFile objects representing uploading files
     */
    public void addInvoice(String invoiceJson, List<MultipartFile> files) {
        try {
            @Valid Invoice invoice = invoiceUtils.convertJsonStringToObject(invoiceJson);
            List<File> invoiceFiles = invoiceUtils.extractFilesDetails(files);
            invoice.setFiles(invoiceFiles);
            invoiceRepository.save(invoice);
            logService.createAddLog(invoice);
        } catch (Exception e) {
            LOGGER.error("Something went wrong when add new invoice: " + e.getMessage());
            throw new InvoiceNotAddedException(e.getMessage());
        }
    }

    /**
     * Retrieves a paginated list of invoices that was not deleted.
     *
     * @param pageNumber   the page number to retrieve
     * @return a Page object with the list of invoices
     */
    public Page<InvoiceDto> findAllPaging(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Invoice> invoices = invoiceRepository.findAll(pageable);

        List<InvoiceDto> filteredInvoice = invoices.getContent().stream()
                .filter(invoice -> !invoice.isDeleted())
                .map(invoice -> mapper.toDto(invoice, InvoiceDto.class))
                .toList();

        if (filteredInvoice.isEmpty()) {
            LOGGER.error("No invoice for page number: " + pageNumber);
            throw new NoInvoicesForThisPageException("No invoices for page number: " + pageNumber);
        }
        return new PageImpl<>(filteredInvoice, pageable, filteredInvoice.size());
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id   the ID of the invoice
     * @return the retrieved invoice
     * @throws InvoiceNotExistException if the invoice does not exist or the invoice deleted from system.
     */
    @Override
    public InvoiceDto findById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (invoice.isEmpty()) {
            LOGGER.error("No any invoice with id = " + id);
            throw new InvoiceNotExistException("No invoice exists for this id: " + id);
        }
        if (invoice.get().isDeleted()) {
            LOGGER.error("The invoice with id = " + id + " has deleted!");
            throw new InvoiceNotExistException("Invoice deleted from the system!");
        }

        return mapper.toDto(invoice.get(), InvoiceDto.class);
    }

    /**
     * Updates an invoice, Save the invoice with updated data.
     * Create new update log and store important info about this action.
     *
     * @param invoiceDto   the Invoice object representing the updated invoice
     * @throws InvoiceNotExistException if the invoice does not exist
     */
    @Override
    public void update(InvoiceDto invoiceDto) {
        if (!invoiceRepository.existsById(invoiceDto.getId())) {
            LOGGER.error("The invoice with id = " + invoiceDto.getId() + " Not exist");
            throw new InvoiceNotExistException("Invoice does not exist!");
        }
        Invoice invoice = mapper.toModel(invoiceDto, Invoice.class);
        invoiceRepository.save(invoice);
        logService.createUpdateLog(invoice);
    }

    /**
     * Deletes an invoice by its ID,
     * This done by convert the boolean is_deleted to true in the invoices table in db.
     * Create delete log and this log represent important info about this action.
     *
     * @param id   the ID of the invoice to delete
     * @throws InvoiceNotExistException if the invoice does not exist or is deleted
     */
    @Override
    public void deleteById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (invoice.isEmpty()) {
            LOGGER.error("Invoice with id = " + id + " Not exist");
            throw new InvoiceNotExistException("Invoice does not exist with id: " + id);
        }

        invoiceRepository.softDeleteById(id);
        logService.createDeleteLog(invoice.get());
    }

    /**
     * Retrieves a list of all invoices the was not deleted.
     *
     * @return a list of all invoices
     */
    @Override
    public List<InvoiceDto> findAll() {
        List<Invoice> invoices = invoiceRepository.findAll();

        if (invoices.isEmpty()) {
            LOGGER.error("No any invoice in the system!");
            throw new InvoiceNotExistException("No invoice exists in the system!");
        }

        List<InvoiceDto> invoicesDto = invoices.stream()
                .filter(invoice -> !invoice.isDeleted())
                .map(invoice -> mapper.toDto(invoice, InvoiceDto.class))
                .toList();

        if (invoicesDto.isEmpty()){
            LOGGER.error("All invoices are deleted from the system!");
            throw new InvoiceNotExistException("All invoices are deleted from the system!");
        }

        return invoicesDto;
    }

    /**
     * Retrieves an invoices by the user ID.
     *
     * @param id   the ID of the user
     * @return a list of invoices associated of this user
     * @throws InvoiceNotExistException if there are no invoices for this user
     */
    public List<InvoiceDto> findByUserId(Long id) {
        List<Invoice> invoices = invoiceRepository.findByUserId(id);

        if (invoices == null || invoices.isEmpty()) {
            throw new InvoiceNotExistException("No invoices exist for the user with id: " + id);
        }

        List<InvoiceDto> invoicesDto = invoices.stream()
                .filter(invoice -> !invoice.isDeleted())
                .map(invoice -> mapper.toDto(invoice, InvoiceDto.class))
                .toList();

        if(invoicesDto.isEmpty()){
            LOGGER.error("No any invoice in the system for the user with id = " + id);
            throw new InvoiceNotExistException("No any invoice in the system for the user with id = " + id);
        }

        return invoicesDto;
    }
}