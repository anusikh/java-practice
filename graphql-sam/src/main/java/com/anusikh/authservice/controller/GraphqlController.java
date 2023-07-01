package com.anusikh.authservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.anusikh.authservice.entity.Book;
import com.anusikh.authservice.entity.Rating;
import com.anusikh.authservice.service.BookService;
import com.anusikh.authservice.service.RatingService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class GraphqlController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RatingService ratingService;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<Book> getBooks() {
        return bookService.getBooks();
    }

    // This is one method of solving n+1
    @BatchMapping
    public Map<Book, List<Rating>> ratings(List<Book> books) {
        log.info("batch call for books", books);
        return ratingService.ratingForBooks(books);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Book createBook(@Argument String name, @Argument String author, @Argument String publisher,
            @Argument Double price) {
        return bookService.addBook(name, author, publisher, price);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Rating createRating(@Argument int bookId, @Argument int rating, @Argument String comment,
            @Argument Long user) {
        return ratingService.addRating(bookId, rating, comment, user);
    }
}
