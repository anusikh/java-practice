package com.anusikh.authservice.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anusikh.authservice.entity.Book;
import com.anusikh.authservice.entity.Rating;
import com.anusikh.authservice.repository.RatingRepository;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating addRating(int bookId, int rating, String comment, Long user) {
        Rating r = new Rating(bookId, rating, comment, user);
        return ratingRepository.save(r);
    }

    public Map<Book, List<Rating>> ratingForBooks(List<Book> books) {
        return books.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        book -> ratingRepository.findAll().stream()
                                .filter(x -> x.getBookId() == book.getId())
                                .toList()));
    }

}
