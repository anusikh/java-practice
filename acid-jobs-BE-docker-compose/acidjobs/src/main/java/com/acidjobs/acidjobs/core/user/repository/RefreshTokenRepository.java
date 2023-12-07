package com.acidjobs.acidjobs.core.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.RefreshToken;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	RefreshToken findByToken(String token);
	int deleteByUser(User user);
}
