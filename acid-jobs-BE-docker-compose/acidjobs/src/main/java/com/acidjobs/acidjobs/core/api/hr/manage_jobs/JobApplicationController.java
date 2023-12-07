package com.acidjobs.acidjobs.core.api.hr.manage_jobs;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.Jobs;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.JobsServices;
import com.acidjobs.acidjobs.core.api.user.applied_job.AppliedJobService;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@CrossOrigin
@RestController
@RequestMapping("/company")
public class JobApplicationController {
	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private JobsServices jobsServices;

	@Autowired
	private AppliedJobService appliedJobService;

	@GetMapping("/job/{job_id}/applications")
	public GenericResponse getJobApplications(@PathVariable("job_id") Long id, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		Optional<Jobs> jobs = jobsServices.getJobById(id);
		if (jobs.isPresent()) {
			List<Object> users = appliedJobService.findUsersByJobId(id);

			JobApplicationResponse jobApplicationResponse = new JobApplicationResponse(users,jobs.get());
			return new GenericResponse("success", "This job is no longer available", jobApplicationResponse);

		}
		return new GenericResponse("info", "This job is no longer available", null);

	}

}
