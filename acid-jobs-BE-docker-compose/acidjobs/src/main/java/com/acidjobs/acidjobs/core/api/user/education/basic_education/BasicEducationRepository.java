package com.acidjobs.acidjobs.core.api.user.education.basic_education;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface BasicEducationRepository extends JpaRepository<BasicEducation,Long> {

	List<BasicEducation> findByUser(User user);
	BasicEducation findByUserAndQualification(User user ,String qualification);
}
