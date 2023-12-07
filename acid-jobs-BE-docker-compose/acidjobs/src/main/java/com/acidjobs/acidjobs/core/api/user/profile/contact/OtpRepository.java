package com.acidjobs.acidjobs.core.api.user.profile.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface OtpRepository extends JpaRepository<OTP ,Long> {

	OTP findByUserAndMobile(User user,String mobile);
}
