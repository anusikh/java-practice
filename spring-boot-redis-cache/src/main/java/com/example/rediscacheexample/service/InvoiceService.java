package com.example.rediscacheexample.service;

import com.example.rediscacheexample.entity.Invoice;
import java.util.List;

public interface InvoiceService {
    
    public Invoice saveInvoice(Invoice invoice);
    public Invoice updateInvoice(Invoice invoice, Integer invId);
    public void deleteInvoice(Integer invId);
    public Invoice getOnceInvoice(Integer invId);
    public List<Invoice> getAllInvoices();
}
