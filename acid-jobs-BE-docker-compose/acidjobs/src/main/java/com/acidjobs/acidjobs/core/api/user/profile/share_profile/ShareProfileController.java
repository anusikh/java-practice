package com.acidjobs.acidjobs.core.api.user.profile.share_profile;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.acidjobs.acidjobs.core.api.user.preview.PreviewResponse;
import com.acidjobs.acidjobs.core.api.user.profile.contact.UserPhoneRepository;
import com.acidjobs.acidjobs.core.api.user.profile.profile_image.ProfileImage;
import com.acidjobs.acidjobs.core.api.user.profile.profile_image.ProfileImageRepository;
import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.api.user.resume.ResumeRepository;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccount;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccountRepository;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.repository.UserRepository;
import com.acidjobs.acidjobs.exception.GenericException;

@RestController
@CrossOrigin
public class ShareProfileController {

	@Autowired
	private UserRepository userRepository;

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
	private ProfileImageRepository profileImageRepository;

	@GetMapping("/profile/{email}")
	public ResponseEntity<UserInfo> getUserInformation(@PathVariable("email") String email) throws GenericException {
		User user=userRepository.findByEmail(email);

		try{
			ProfileImage profileImage=profileImageRepository.findByUser(user);
			UserData userData =new UserData();
			userData.setEmail(user.getEmail());
			userData.setFirstName(user.getFirstName());
			userData.setLastName(user.getLastName());
			userData.setProfileImage(profileImage.getFileName());
			userData.setImageUrl(profileImage.getUrl());

			List<Graduation> graduations = graduationRepository.findByUser(user);
			List<BasicEducation> basicEducations = basicEducationRepository.findByUser(user);
			List<Skills> skills = skillsRepository.findByUser(user);
			List<Project> projects = projectRepository.findByUser(user);
			List<SocialAccount> socialAccounts = socialAccountRepository.findByUser(user);
			List<Employment> employments = employmentRepository.findByUser(user);
			PersonalDetails personalDetails = personalDetailsRepository.findByUser(user);
			Resume resume = resumeRepository.findByUser(user);
			if(isaBoolean(profileImage, graduations, basicEducations, skills, projects, socialAccounts, employments, personalDetails, resume)
			){
				return ResponseEntity.ok(new UserInfo(userData,graduations, basicEducations, skills, projects, socialAccounts, employments, personalDetails, resume));
			
			}
			return ResponseEntity.badRequest().body(null);
		}
		catch (Exception ex){
			throw new GenericException("This user not available");
		}

	}

	private boolean isaBoolean(ProfileImage profileImage, List<Graduation> graduations, List<BasicEducation> basicEducations, List<Skills> skills, List<Project> projects, List<SocialAccount> socialAccounts, List<Employment> employments, PersonalDetails personalDetails, Resume resume) {
		return (graduations.size() > 0) && (basicEducations.size() > 0) && (skills.size() > 0) && (projects.size() > 0) && (socialAccounts.size() > 0) && (employments.size() > 0) && (personalDetails != null) && (resume != null) && (profileImage != null);
	}

}
