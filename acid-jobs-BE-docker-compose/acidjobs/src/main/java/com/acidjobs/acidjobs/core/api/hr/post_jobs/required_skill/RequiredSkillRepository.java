package com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;

public interface RequiredSkillRepository extends JpaRepository<RequiredSkill, Long> {
	List<RequiredSkill> findByJobs(Jobs jobs);

	@Transactional
	Long deleteByJobs(Jobs jobs);

	RequiredSkill findByJobsAndSkill(Jobs jobs, String skill);
}
