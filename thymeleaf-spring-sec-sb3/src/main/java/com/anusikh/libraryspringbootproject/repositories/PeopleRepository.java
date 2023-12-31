package com.anusikh.libraryspringbootproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.anusikh.libraryspringbootproject.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonById(Long id);
    List<Person> findAllByRemovedIsFalseOrderByIdAsc();
    List<Person> findAllByRemovedIsFalseAndPasswordIsNotNullOrderByIdAsc();
    Optional<Person> findByName(String name);
}
