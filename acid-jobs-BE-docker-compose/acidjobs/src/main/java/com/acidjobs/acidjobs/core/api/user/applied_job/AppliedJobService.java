package com.acidjobs.acidjobs.core.api.user.applied_job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.pojo.response.UserResponse;

@Service
public class AppliedJobService {

	@Autowired
	private AppliedJobRepository appliedJobRepository;

	public boolean save(AppliedJob appliedJob) {
		AppliedJob appliedJob1 = appliedJobRepository.findByUserAndJobs(appliedJob.getUser(), appliedJob.getJobs());
		if (null == appliedJob1) {
			appliedJobRepository.save(appliedJob);
			return true;
		}
		return false;
	}

	public long getByJob(Jobs jobs1) {
       return appliedJobRepository.countByJobs(jobs1);
	}
	public List<Object> findUsersByJobId(Long id ){
		return appliedJobRepository.findUserByJobId(id);
	}

	public List<Long> getAllAppliedJobIdByUser(User user) {
		return appliedJobRepository.findAllAppliedJobIdByUser(user.getId());
	}
}
