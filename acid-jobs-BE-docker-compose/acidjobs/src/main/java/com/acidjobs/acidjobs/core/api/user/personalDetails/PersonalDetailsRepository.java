package com.acidjobs.acidjobs.core.api.user.personalDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails ,Long> {
	PersonalDetails findByUser(User user);
}
