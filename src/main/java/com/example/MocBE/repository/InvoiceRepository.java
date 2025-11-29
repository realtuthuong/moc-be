package com.example.MocBE.repository;

import com.example.MocBE.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository  extends JpaRepository<Invoice, UUID> {
}
