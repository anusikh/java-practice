package com.anusikh.libraryspringbootproject.exception_person;

public class PersonNotUpdatedException  extends RuntimeException {
    public PersonNotUpdatedException(String message) {
        super(message);
    }
}
