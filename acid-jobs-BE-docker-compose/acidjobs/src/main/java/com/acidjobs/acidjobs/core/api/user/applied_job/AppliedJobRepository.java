package com.acidjobs.acidjobs.core.api.user.applied_job;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface AppliedJobRepository extends JpaRepository<AppliedJob, Long> {
	AppliedJob findByUserAndJobs(User user, Jobs jobs);

	long countByJobs(Jobs jobs);

	@Query(value = "SELECT u.first_name , u.last_name  , u.email,aj.date FROM user u INNER JOIN  applied_job aj on u.id=aj.user_id WHERE aj.job_id=:jobId", nativeQuery = true)
	List<Object> findUserByJobId(Long jobId);

	@Query(value = "SELECT aj.job_id FROM applied_job aj WHERE aj.user_id=:userId", nativeQuery = true)
	List<Long> findAllAppliedJobIdByUser(Long userId);
}
