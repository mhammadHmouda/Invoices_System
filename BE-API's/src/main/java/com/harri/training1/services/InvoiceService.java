package com.harri.training1.services;

import com.harri.training1.exceptions.InvoiceNotAddedException;
import com.harri.training1.exceptions.InvoiceNotExistException;
import com.harri.training1.exceptions.NoInvoicesForThisPageException;
import com.harri.training1.models.entities.File;
import com.harri.training1.models.entities.Invoice;
import com.harri.training1.repositories.InvoiceRepository;
import com.harri.training1.utils.InvoiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final LogService logService;

    public void addInvoice(String invoiceJson, List<MultipartFile> files){
        try {
            Invoice invoice = InvoiceUtils.convertJsonStringToObject(invoiceJson);
            List<File> invoiceFiles = InvoiceUtils.extractFilesDetails(files);
            invoice.setFiles(invoiceFiles);
            invoiceRepository.save(invoice);
            logService.createAddLog();
        }
        catch (Exception e) {
            throw new InvoiceNotAddedException(e.getMessage());
        }
    }

    public Page<Invoice> findAll(int pageNumber){
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("createdAt").descending());

        Page<Invoice> invoices = invoiceRepository.findAll(pageable);

        if (!invoices.hasContent())
            throw new NoInvoicesForThisPageException("No invoices for page number: " + pageNumber);

        return invoices;
    }

    public Invoice findById(Long id){
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (invoice.isEmpty())
            throw new InvoiceNotExistException("No invoice exist for this id: " + id);

        return invoice.get();
    }

    public void updateInvoice(Invoice invoice){
        if(!invoiceRepository.existsById(invoice.getId())) {
            throw new InvoiceNotExistException("Invoice not exist!");
        }

        invoiceRepository.save(invoice);
        logService.createUpdateLog();
    }

    public void deleteById(Long id) {
        if (!invoiceRepository.existsById(id))
            throw new InvoiceNotExistException("Invoice not exist of id: "  + id);

        invoiceRepository.deleteById(id);
        logService.createDeleteLog();
    }

    public List<Invoice> findByUserId(Long id) {
        List<Invoice> invoices = invoiceRepository.findByUserId(id);

        if (invoices == null || invoices.isEmpty()){
            throw new InvoiceNotExistException("Invoice not exist for the user with id: " + id);
        }

        return invoices;
    }
}
