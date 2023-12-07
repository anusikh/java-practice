package com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;

@Repository
public interface RequiredQualificationRepository extends JpaRepository<RequiredQualification,Long> {
	List<RequiredQualification> findByJobs(Jobs jobs);
	@Transactional
	Long deleteByJobs(Jobs jobs);

	RequiredQualification findByJobsAndCourse(Jobs jobs, String course);

}
