package com.acidjobs.acidjobs.core.api.user.education.graduation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

public interface GraduationRepository extends JpaRepository<Graduation,Long> {
    List<Graduation> findByUser(User user);
	Graduation findByUserAndQualification(User user,String qualification);
}
