package com.anusikh.libraryspringbootproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.anusikh.libraryspringbootproject.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByPersonIdOrderByIdAsc(Long id);
    Optional<Book> findBookById(Long id);
    List<Book> findAllByRemovedIsFalseOrderByIdAsc();

    Optional<Book> findFirstByName(String Name);
}
