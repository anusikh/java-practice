package com.acidjobs.acidjobs.core.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

public interface UserRepository extends JpaRepository<User,Long> {

	User findByEmail(String email);
}
