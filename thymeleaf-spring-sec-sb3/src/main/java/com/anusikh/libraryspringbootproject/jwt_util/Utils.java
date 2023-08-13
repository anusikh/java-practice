package com.anusikh.libraryspringbootproject.jwt_util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.anusikh.libraryspringbootproject.dto.PersonDTO;
import com.anusikh.libraryspringbootproject.model.Person;
@Component
public class Utils {
    private final ModelMapper modelMapper;
    @Autowired
    public Utils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
