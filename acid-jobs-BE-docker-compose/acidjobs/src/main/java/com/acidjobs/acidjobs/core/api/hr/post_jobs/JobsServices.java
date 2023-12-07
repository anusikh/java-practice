package com.acidjobs.acidjobs.core.api.hr.post_jobs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

@Service
public class JobsServices {

	@Autowired
	private JobsRepository jobsRepository;

	public Jobs add(Jobs jobs) {
		return jobsRepository.save(jobs);
	}

	public List<Jobs> getJobs(User user,int page) {
		Pageable firstPageTenRecord= PageRequest.of(page,10, Sort.by("date").descending());
		return jobsRepository.findByUser(user,firstPageTenRecord).getContent();
	}

	public long getTotalJobs(User user) {
		Pageable firstPageTenRecord= PageRequest.of(0,10, Sort.by("date").descending());
		return jobsRepository.findByUser(user,firstPageTenRecord).getTotalElements();
	}

	public int getTotalPages(User user) {
		Pageable firstPageTenRecord= PageRequest.of(0,10, Sort.by("date").descending());
		return jobsRepository.findByUser(user,firstPageTenRecord).getTotalPages();
	}

	public Jobs update(Jobs jobs) {
		Jobs job=jobsRepository.findById(jobs.getId()).get();
		if(job!=null){
			job.setCity(jobs.getCity());
			job.setDescription(jobs.getDescription());
			job.setJobsType(jobs.getJobsType());
			job.setExperience(jobs.getExperience());
			job.setLocation(jobs.getLocation());
			job.setTitle(jobs.getTitle());
			job.setTags(jobs.getTags());
			job.setSalary(jobs.getSalary());
			if (!jobs.getFile()
					 .equals("")) {
				job.setFile(jobs.getFile());
				job.setFile_url(jobs.getFile_url());
			}
			jobsRepository.save(job);
			return job;
		}
		return job;
	}

	public Optional<Jobs> getJobById(Long id) {
		return jobsRepository.findById(id);
	}

	public Page<Jobs> getAllAppliedJobsByJobIds(List<Long> jobIds,Pageable pageable) {
		return  jobsRepository.findByIdIn(jobIds,pageable);
	}
}
