package com.acidjobs.acidjobs.core.api.user.applied_job;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.JobsRepository;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.JobsServices;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualification;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualificationRepository;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkill;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkillRepository;
import com.acidjobs.acidjobs.core.api.user.jobs.UserJobs;
import com.acidjobs.acidjobs.core.api.user.jobs.UserJobsResponse;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class AppliedJobController {
	@Autowired
	private AppliedJobService appliedJobService;

	@Autowired
	private JobsServices jobsServices;
	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private JobsRepository jobsRepository;
	@Autowired
	private RequiredSkillRepository requiredSkillRepository;

	@Autowired
	private AppliedJobRepository appliedJobRepository;

	@Autowired
	private RequiredQualificationRepository requiredQualificationRepository;

	@PostMapping("/apply-job")
	public GenericResponse saveAppliedJob(@RequestParam("job_id") Long id, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		Optional<Jobs> jobs = jobsServices.getJobById(id);
		if (jobs.isPresent()) {
			AppliedJob appliedJob = new AppliedJob(null, Instant.now(), jobs.get(), user);
			boolean isApplied = appliedJobService.save(appliedJob);
			if (isApplied) {
				var userJobsResponse = getAllUnAppliedJob(0, user.getId());
				return new GenericResponse("success","Job applied successfully", userJobsResponse);

			}
			return new GenericResponse("info", "You have already applied this job", null);

		}
		return new GenericResponse("error", "This job is no longer available", null);
	}

	private UserJobsResponse getAllUnAppliedJob(int page,Long userId){
		List<Long> jobsIds=appliedJobRepository.findAllAppliedJobIdByUser(userId);
		Pageable firstPageTenRecord= PageRequest.of(page,10, Sort.by("date").descending());
		Page<Jobs> jobs= jobsRepository.findByIdNotIn(jobsIds,firstPageTenRecord);
		List<UserJobs> userjobs=new ArrayList<>();
		for(Jobs jobs1:jobs.getContent()){
			List<RequiredQualification> qualifications=requiredQualificationRepository.findByJobs(jobs1);
			List<RequiredSkill> skills=requiredSkillRepository.findByJobs(jobs1);
			Duration duration = Duration.between(Instant.now(), jobs1.getDate());
			long days=duration.toDays();
			userjobs.add(new UserJobs(jobs1,days, skills, qualifications));
		}
		int totalPages=jobs.getTotalPages();
		long totaljobs=jobs.getTotalElements();
		UserJobsResponse userJobsResponse=new UserJobsResponse(userjobs,totaljobs,totalPages);
		return userJobsResponse;

	}

	@GetMapping("/applied-jobs/{page}")
	public GenericResponse getAppliedJobsByUser(@PathVariable("page")int page,HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
	    List<Long> jobIds=appliedJobService.getAllAppliedJobIdByUser(user);
		Pageable firstPageTenRecord= PageRequest.of(page,10, Sort.by("date").descending());
		Page<Jobs> appliedJobs=jobsServices.getAllAppliedJobsByJobIds(jobIds,firstPageTenRecord);
		List<UserJobs> userjobs=new ArrayList<>();
		for(Jobs jobs1:appliedJobs.getContent()){
			List<RequiredQualification> qualifications=requiredQualificationRepository.findByJobs(jobs1);
			List<RequiredSkill> skills=requiredSkillRepository.findByJobs(jobs1);
			Duration duration = Duration.between(Instant.now(), jobs1.getDate());
			long days=duration.toDays();
			userjobs.add(new UserJobs(jobs1,days, skills, qualifications));
		}
		int totalPages=appliedJobs.getTotalPages();
		long totaljobs=appliedJobs.getTotalElements();
		UserJobsResponse userJobsResponse=new UserJobsResponse(userjobs,totaljobs,totalPages);
		return new GenericResponse("success","Job applied successfully", userJobsResponse);


	}

}
