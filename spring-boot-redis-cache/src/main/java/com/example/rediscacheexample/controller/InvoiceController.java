package com.example.rediscacheexample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rediscacheexample.entity.Invoice;
import com.example.rediscacheexample.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/saveInv")
    public Invoice saveInv(@RequestBody Invoice inv) {
        return invoiceService.saveInvoice(inv);
    }

    @GetMapping("/allInv")
    public ResponseEntity<List<Invoice>> allInv() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());    
    }

    @GetMapping("/getOne/{id}")
    public Invoice getOne(@PathVariable Integer id) {
        return invoiceService.getOnceInvoice(id);
    }
    
    @PutMapping("/modify/{id}")
    public Invoice modify(@RequestBody Invoice inv, @PathVariable Integer id) {
        return invoiceService.updateInvoice(inv, id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        invoiceService.deleteInvoice(id);
        return "Invoice with id:" + id + " Deleted!";
    }
}
