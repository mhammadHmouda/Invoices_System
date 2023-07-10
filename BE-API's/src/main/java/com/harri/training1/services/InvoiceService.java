package com.harri.training1.services;

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

    public void addInvoice(Invoice invoice, List<MultipartFile> files){
        List<File> invoiceFiles = InvoiceUtils.extractFileDetails(files);
        invoice.setFiles(invoiceFiles);

        invoiceRepository.save(invoice);
    }

    public Page<Invoice> findAll(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        return invoiceRepository.findAll(pageable);
    }

    public Invoice findById(Long id){
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        return invoice.orElse(null);
    }
}
