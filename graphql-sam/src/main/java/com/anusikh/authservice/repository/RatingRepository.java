package com.anusikh.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anusikh.authservice.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
