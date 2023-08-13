package com.anusikh.libraryspringbootproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.anusikh.libraryspringbootproject.model.Person;
import com.anusikh.libraryspringbootproject.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Optional;



@Component
public class MyUserDetailsService implements UserDetailsService {

    private PeopleRepository userRepo;
    @Autowired
    public MyUserDetailsService(PeopleRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Person> userRes = userRepo.findByName(name);
        if(userRes.isEmpty())
            throw new UsernameNotFoundException("Не найден пользователь = " + name);
        Person user = userRes.get();
        return new org.springframework.security.core.userdetails.User(
                name,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
