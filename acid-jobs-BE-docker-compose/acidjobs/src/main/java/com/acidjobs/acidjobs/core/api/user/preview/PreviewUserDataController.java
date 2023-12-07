package com.acidjobs.acidjobs.core.api.user.preview;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.api.user.Project.Project;
import com.acidjobs.acidjobs.core.api.user.Project.ProjectRepository;
import com.acidjobs.acidjobs.core.api.user.Skill.Skills;
import com.acidjobs.acidjobs.core.api.user.Skill.SkillsRepository;
import com.acidjobs.acidjobs.core.api.user.education.basic_education.BasicEducation;
import com.acidjobs.acidjobs.core.api.user.education.basic_education.BasicEducationRepository;
import com.acidjobs.acidjobs.core.api.user.education.graduation.Graduation;
import com.acidjobs.acidjobs.core.api.user.education.graduation.GraduationRepository;
import com.acidjobs.acidjobs.core.api.user.employment.Employment;
import com.acidjobs.acidjobs.core.api.user.employment.EmploymentRepository;
import com.acidjobs.acidjobs.core.api.user.personalDetails.PersonalDetails;
import com.acidjobs.acidjobs.core.api.user.personalDetails.PersonalDetailsRepository;
import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.api.user.resume.ResumeRepository;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccount;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccountRepository;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class PreviewUserDataController {

	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private SkillsRepository skillsRepository;

	@Autowired
	private SocialAccountRepository socialAccountRepository;

	@Autowired
	private GraduationRepository graduationRepository;

	@Autowired
	private BasicEducationRepository basicEducationRepository;

	@Autowired
	private ResumeRepository resumeRepository;
	@Autowired
	private EmploymentRepository employmentRepository;

	@Autowired
	private JWTUtility jwtUtility;


	@GetMapping("/preview")
	public PreviewResponse get(HttpServletRequest httpServletRequest) throws GenericException {
		try{
			String authorization = httpServletRequest.getHeader("Authorization");
			User user= jwtUtility.getUserFromToken(authorization);
			List<Graduation> graduations=graduationRepository.findByUser(user);
			List<BasicEducation> basicEducations=basicEducationRepository.findByUser(user);
			List<Skills> skills=skillsRepository.findByUser(user);
			List<Project> projects=projectRepository.findByUser(user);
			List<SocialAccount> socialAccounts=socialAccountRepository.findByUser(user);
			List<Employment> employments=employmentRepository.findByUser(user);
			PersonalDetails personalDetails=personalDetailsRepository.findByUser(user);
			Resume resume=resumeRepository.findByUser(user);
			return new PreviewResponse(graduations,basicEducations,skills,projects,socialAccounts,employments,personalDetails,resume);

		}
		catch(Exception ex){
			throw new GenericException("Complete your profile first !!");
		}
	}
}

