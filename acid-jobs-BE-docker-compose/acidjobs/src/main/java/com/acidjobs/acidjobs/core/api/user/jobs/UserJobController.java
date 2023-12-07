package com.acidjobs.acidjobs.core.api.user.jobs;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.JobsRepository;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualification;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualificationRepository;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkill;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkillRepository;
import com.acidjobs.acidjobs.core.api.user.applied_job.AppliedJobRepository;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserJobController {
	@Autowired
	private JobsRepository jobsRepository;

	@Autowired
	private RequiredQualificationRepository requiredQualificationRepository;
	@Autowired
	private RequiredSkillRepository requiredSkillRepository;

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private AppliedJobRepository appliedJobRepository;

	@GetMapping("/jobs/{page_no}")
	public GenericResponse findJobs(@PathVariable("page_no") int page, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);

		List<Long> jobsIds = appliedJobRepository.findAllAppliedJobIdByUser(user.getId());
		Pageable firstPageTenRecord = PageRequest.of(page, 10, Sort.by("date")
																   .descending());
		Page<Jobs> jobs;
		if (jobsIds.isEmpty()) {
			jobs = jobsRepository.findAll(firstPageTenRecord);
		} else {

			jobs = jobsRepository.findByIdNotIn(jobsIds, firstPageTenRecord);
		}

		List<UserJobs> userjobs = new ArrayList<>();
		for (Jobs jobs1 : jobs.getContent()) {

			List<RequiredQualification> qualifications = requiredQualificationRepository.findByJobs(jobs1);
			List<RequiredSkill> skills = requiredSkillRepository.findByJobs(jobs1);
			Duration duration = Duration.between(Instant.now(), jobs1.getDate());
			long days = duration.toDays();
			userjobs.add(new UserJobs(jobs1, days, skills, qualifications));
		}
		int totalPages = jobs.getTotalPages();
		long totaljobs = jobs.getTotalElements();
		UserJobsResponse userJobsResponse = new UserJobsResponse(userjobs, totaljobs, totalPages);
		return new GenericResponse("success", "", userJobsResponse);
	}

}
