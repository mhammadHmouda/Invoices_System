package com.harri.training1.repositories;

import com.harri.training1.models.entities.Invoice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("SELECT i FROM Invoice i WHERE i.user.id = :id")
    List<Invoice> findByUserId(@Param(value = "id") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.isDeleted = true WHERE i.id = :id")
    void softDeleteById(@Param("id") Long invoiceId);

    List<Invoice> findByTotalPrice(float totalPrice);

    List<Invoice> findByTotalPriceIsGreaterThan(float totalPrice);
}
