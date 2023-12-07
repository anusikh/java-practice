package com.acidjobs.acidjobs.core.api.user.resume;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {
	Resume findByUser(User user);
}
