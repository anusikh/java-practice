package com.anusikh.authservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "rating_tbl")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int bookId;
    private int rating;
    private String comment;
    private Long user;

    public Rating(int bookId, int rating, String comment, Long user) {
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }
}
