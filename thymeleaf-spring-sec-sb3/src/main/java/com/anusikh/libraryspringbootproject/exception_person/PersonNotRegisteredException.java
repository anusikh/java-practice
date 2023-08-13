package com.anusikh.libraryspringbootproject.exception_person;

public class PersonNotRegisteredException extends RuntimeException {
    public PersonNotRegisteredException(String message) {
        super(message);
    }
}
