package com.acidjobs.acidjobs.core.api.hr.post_jobs;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualification;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_qualification.RequiredQualificationService;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkill;
import com.acidjobs.acidjobs.core.api.hr.post_jobs.required_skill.RequiredSkillService;
import com.acidjobs.acidjobs.core.api.user.applied_job.AppliedJobService;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.FileUploader;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class JobsController {

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private FileUploader fileUploader;

	@Autowired
	private JobsServices jobsServices;

	@Autowired
	private RequiredSkillService requiredSkillService;
	@Autowired
	private RequiredQualificationService requiredQualificationService;

	@Autowired
	private JobsRepository jobsRepository;

	@Autowired
	private AppliedJobService appliedJobService;

	@PostMapping("/save-jobs")
	public GenericResponse addSave(@RequestParam("title") String title, @RequestParam("description") String email, @RequestParam("tags") String tags, @RequestParam("jobsType") String jobsType, @RequestParam("experience") String experience, @RequestParam("salary") long salary, @RequestParam("city") String city, @RequestParam("location") String location, @RequestParam("skills") String skills, @RequestParam("qualifications") String qualifications, @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);

		boolean is_uploaded = fileUploader.upload(file, user.getId());
		if (!file.isEmpty()) {
			if (is_uploaded) {
				String url = ServletUriComponentsBuilder.fromCurrentContextPath()
														.path("/file/" + user.getId() + "/" + file.getOriginalFilename())
														.toUriString();
				Jobs jobs = new Jobs(null, title, email, tags, jobsType, experience, salary, city, location, Instant.now(), file.getOriginalFilename(), url, user);
				Jobs jobs1 = jobsServices.add(jobs);

				if (jobs1 != null) {
					JSONArray skill = new JSONObject(skills).getJSONArray("data");
					JSONArray qualification = new JSONObject(qualifications).getJSONArray("data");
					for (int i = 0; i < skill.length(); i++) {
						RequiredSkill requiredSkill = new RequiredSkill(null, skill.getJSONObject(i)
																				   .getString("skill"), jobs1);
						requiredSkillService.add(requiredSkill);
					}
					for (int i = 0; i < qualification.length(); i++) {
						RequiredQualification requiredQualification = new RequiredQualification(null, qualification.getJSONObject(i)
																												   .getString("course"), qualification.getJSONObject(i)
																																					  .getDouble("percentage"), jobs1);
						requiredQualificationService.add(requiredQualification);
					}
					return new GenericResponse("success", "New job post successfully", null);
				}
			}
			return new GenericResponse("error", "Failed to add new jobs!!!", null);
		}

		return new GenericResponse("error", "File should not be empty", null);
	}

	@GetMapping("/job/{id}")
	public GenericResponse getJob(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);

		Jobs jobs = jobsRepository.findById(id)
								  .get();
		List<RequiredSkill> skills = requiredSkillService.getByJob(jobs);
		List<RequiredQualification> qualifications = requiredQualificationService.getByJob(jobs);
		long noOfApplication = appliedJobService.getByJob(jobs);
		Job job = new Job(jobs, skills, qualifications, noOfApplication);
		return new GenericResponse("success", "", job);

	}

	@PostMapping("/delete-job")
	public GenericResponse deleteJob(@RequestParam("id") Long id, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		fileUploader.delete(id);
		Jobs jobs2 = jobsRepository.findById(id)
								   .get();
		boolean deleteSkill = requiredSkillService.delete(jobs2);
		boolean deleteQualification = requiredQualificationService.delete(jobs2);

		if (deleteSkill && deleteQualification) {
			jobsRepository.delete(jobs2);
			List<Jobs> jobsList = jobsServices.getJobs(user, 0);
			List<Job> jobs = new ArrayList<>();
			for (Jobs jobs1 : jobsList) {
				List<RequiredQualification> qualifications = requiredQualificationService.getByJob(jobs1);
				List<RequiredSkill> skills = requiredSkillService.getByJob(jobs1);
				long noOfApplication = appliedJobService.getByJob(jobs1);
				jobs.add(new Job(jobs1, skills, qualifications, noOfApplication));
			}
			long totaljobs = jobsList.size();
			int totalPages = (int) (totaljobs / 10);

			JobsResponse jobsResponse = new JobsResponse(jobs, totaljobs, totalPages);
			return new GenericResponse("success", "job deleted successfully", jobsResponse);
		}
		return new GenericResponse("error", "failed to delete job", null);
	}

	@PostMapping("/update-job")
	public GenericResponse updateJob(@RequestParam("id") Long id, @RequestParam("title") String title, @RequestParam("description") String email, @RequestParam("tags") String tags, @RequestParam("jobsType") String jobsType, @RequestParam("experience") String experience, @RequestParam("salary") long salary, @RequestParam("city") String city, @RequestParam("location") String location, @RequestParam("skills") String skills, @RequestParam("qualifications") String qualifications, @Nullable @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		Jobs jobs = new Jobs(id, title, email, tags, jobsType, experience, salary, city, location, Instant.now(), "", "", user);

		String url;
		if (file != null) {
			boolean is_uploaded = fileUploader.upload(file, user.getId());

			if (is_uploaded) {
				url = ServletUriComponentsBuilder.fromCurrentContextPath()
												 .path("/file/" + user.getId() + "/" + file.getOriginalFilename())
												 .toUriString();
				jobs.setFile_url(url);
				jobs.setFile(file.getOriginalFilename());
			}

		}

		Jobs jobs1 = jobsServices.update(jobs);
		JSONArray skill = new JSONObject(skills).getJSONArray("data");
		JSONArray qualification = new JSONObject(qualifications).getJSONArray("data");

		List<RequiredSkill> requiredSkills = requiredSkillService.getByJob(jobs1);
		for (RequiredSkill requiredSkill : requiredSkills) {
			boolean found = false;
			for (int i = 0; i < skill.length(); i++) {
				String skillName = skill.getJSONObject(i)
										.getString("skill");
				if (requiredSkill.getSkill()
								 .equals(skillName)) {
					found = true;
					break;
				}

			}
			if (!found) {
				requiredSkillService.deleteSkill(requiredSkill);
			}
		}
		for (int i = 0; i < skill.length(); i++) {
			RequiredSkill requiredSkill = new RequiredSkill(null, skill.getJSONObject(i)
																	   .getString("skill"), jobs1);
			requiredSkillService.addOrUpdate(requiredSkill);
		}

		List<RequiredQualification> requiredQualifications = requiredQualificationService.getByJob(jobs1);
		for (RequiredQualification requiredQualification : requiredQualifications) {
			boolean found = false;
			for (int i = 0; i < qualification.length(); i++) {

				String course = qualification.getJSONObject(i)
											 .getString("course");
				if (requiredQualification.getCourse()
										 .equals(course)) {
					found = true;
					break;
				}
			}
			if (!found) {
				requiredQualificationService.deleteQualification(requiredQualification);
			}
		}
		for (int i = 0; i < qualification.length(); i++) {
			RequiredQualification requiredQualification = new RequiredQualification(null, qualification.getJSONObject(i)
																									   .getString("course"), qualification.getJSONObject(i)
																																		  .getDouble("percentage"), jobs1);
			requiredQualificationService.addOrUpdate(requiredQualification);
		}

		List<RequiredSkill> skillResponse = requiredSkillService.getByJob(jobs1);
		List<RequiredQualification> qualificationResponse = requiredQualificationService.getByJob(jobs1);
		long noOfApplication = appliedJobService.getByJob(jobs1);
		Job job = new Job(jobs1, skillResponse, qualificationResponse, noOfApplication);
		return new GenericResponse("success", "Job updated successfully", job);
	}

	@GetMapping("/get-jobs/{page}")
	public GenericResponse getMyJobs(@PathVariable("page") int page, HttpServletRequest httpServletRequest) throws GenericException {

		String authorization = httpServletRequest.getHeader("Authorization");
		User user = jwtUtility.getUserFromToken(authorization);
		try {
			List<Jobs> jobsList = jobsServices.getJobs(user, page);
			List<Job> jobs = new ArrayList<>();
			for (Jobs jobs1 : jobsList) {
				List<RequiredQualification> qualifications = requiredQualificationService.getByJob(jobs1);
				List<RequiredSkill> skills = requiredSkillService.getByJob(jobs1);
				long noOfApplication = appliedJobService.getByJob(jobs1);
				jobs.add(new Job(jobs1, skills, qualifications, noOfApplication));
			}
			long totaljobs = jobsServices.getTotalJobs(user);
			int totalPages = (int) (totaljobs / 10) + 1;
			JobsResponse jobsResponse = new JobsResponse(jobs, totaljobs, totalPages);
			return new GenericResponse("success", "", jobsResponse);
		} catch (Exception ex) {
			return new GenericResponse("error", "failed load jobs ", null);
		}

	}

}
