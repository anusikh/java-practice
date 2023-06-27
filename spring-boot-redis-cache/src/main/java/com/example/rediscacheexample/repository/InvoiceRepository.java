package com.example.rediscacheexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rediscacheexample.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{
    
}
