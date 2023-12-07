package com.acidjobs.acidjobs.core.api.user.profile.basic_information;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

public interface BasicInformationRepository extends JpaRepository<BasicInformation,Long> {
	BasicInformation findByUser(User user);
}

