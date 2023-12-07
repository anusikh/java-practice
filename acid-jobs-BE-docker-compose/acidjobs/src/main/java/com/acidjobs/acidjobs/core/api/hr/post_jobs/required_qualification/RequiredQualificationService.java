package com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;

@Service
public class RequiredQualificationService {
	@Autowired
	private RequiredQualificationRepository requiredQualificationRepository;


	public List<RequiredQualification> add(RequiredQualification requiredQualification){
		requiredQualificationRepository.save(requiredQualification);
		return requiredQualificationRepository.findByJobs(requiredQualification.getJobs());
	}

	public List<RequiredQualification> getByJob(Jobs jobs1) {
		return  requiredQualificationRepository.findByJobs(jobs1);
	}

	public boolean delete(Jobs jobs) {
		try {
			requiredQualificationRepository.deleteByJobs(jobs);
			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	public void deleteQualification(RequiredQualification requiredQualification) {
		requiredQualificationRepository.delete(requiredQualification);
	}

	public void addOrUpdate(RequiredQualification requiredQualification) {
		RequiredQualification requiredQualification1 = requiredQualificationRepository.findByJobsAndCourse(requiredQualification.getJobs(), requiredQualification.getCourse());
		if (requiredQualification1 == null) {
			requiredQualificationRepository.save(requiredQualification);
		} else {
			requiredQualification1.setPercentage(requiredQualification.getPercentage());
			requiredQualificationRepository.save(requiredQualification1);
		}
	}
}
