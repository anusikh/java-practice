package com.acidjobs.acidjobs.core.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acidjobs.acidjobs.core.user.jpa.data.SecureToken;

public interface SecureTokenRepository extends JpaRepository<SecureToken,Long> {

	SecureToken findByToken(String token);
	Long removeByToken(String token);
}
