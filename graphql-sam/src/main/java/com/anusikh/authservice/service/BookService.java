package com.anusikh.authservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anusikh.authservice.entity.Book;
import com.anusikh.authservice.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(String name, String author, String publisher, Double price) {
        Book b = new Book(name, author, publisher, price);
        return bookRepository.save(b);
    }

    public List<Book> getBooks(){
        return bookRepository.findAll();
    }

}
