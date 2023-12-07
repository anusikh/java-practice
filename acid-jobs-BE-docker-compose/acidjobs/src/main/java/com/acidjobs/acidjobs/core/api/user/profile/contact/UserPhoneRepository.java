package com.acidjobs.acidjobs.core.api.user.profile.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface UserPhoneRepository extends JpaRepository<UserContact,Long>{
	UserContact findByUser(User user);
	UserContact findByUserAndPhoneNumber(User user,String phoneNumber);
	UserContact findByPhoneNumber(String phoneNumber);
}
