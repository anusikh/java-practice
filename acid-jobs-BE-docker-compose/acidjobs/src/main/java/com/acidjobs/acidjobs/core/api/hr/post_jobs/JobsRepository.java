package com.acidjobs.acidjobs.core.api.hr.post_jobs;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Repository
public interface JobsRepository extends JpaRepository<Jobs,Long> {
	Page<Jobs> findByUser(User user, Pageable pageable);
    @Query(value="SELECT * from jobs js WHERE NOT EXISTS (SELECT * FROM applied_job aj WHERE aj.job_id=js.id AND aj.user_id=:user);",
			countQuery = "SELECT count(*) from jobs js WHERE NOT EXISTS (SELECT * FROM applied_job aj WHERE aj.job_id=js.id AND aj.user_id=:user);",
			nativeQuery = true)

	Page<Jobs> findNonAppliedJobs(User user, Pageable pageable);

	Page<Jobs> findByIdNotIn(List<Long>ids,Pageable pageable);
	Page<Jobs> findByIdIn(List<Long>ids,Pageable pageable);
}
