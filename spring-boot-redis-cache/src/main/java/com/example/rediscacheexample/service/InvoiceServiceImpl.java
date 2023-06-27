package com.example.rediscacheexample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.rediscacheexample.entity.Invoice;
import com.example.rediscacheexample.exception.InvoiceNotFoundException;
import com.example.rediscacheexample.repository.InvoiceRepository;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    @CachePut(value="Invoice", key="#invId")
    public Invoice updateInvoice(Invoice invoice, Integer invId) {
        Invoice inv = invoiceRepository.findById(invId).orElseThrow(() -> new InvoiceNotFoundException("Invoice not Found"));
        inv.setInvAmt(invoice.getInvAmt());
        inv.setInvName(invoice.getInvName());
        return invoiceRepository.save(inv);
    }

    @Override
    @CacheEvict(value="Invoice", key="#invId")
    // @CacheEvict(value="Invoice", allEntries = true)
    public void deleteInvoice(Integer invId) {
        Invoice inv = invoiceRepository.findById(invId).orElseThrow(() -> new InvoiceNotFoundException("Invoice not Found"));
        invoiceRepository.delete(inv);
    }

    @Override
    @Cacheable(value="Invoice", key="#invId")
    public Invoice getOnceInvoice(Integer invId) {
        Invoice inv = invoiceRepository.findById(invId).orElseThrow(() -> new InvoiceNotFoundException("Invoice not Found"));
        return inv;
    }

    @Override
    @Cacheable(value="Invoice")
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    
}
