package com.acidjobs.acidjobs.core.api.user.profile.complete_profile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.acidjobs.acidjobs.core.api.user.employment.EmploymentRepository;
import com.acidjobs.acidjobs.core.api.user.personalDetails.PersonalDetails;
import com.acidjobs.acidjobs.core.api.user.personalDetails.PersonalDetailsRepository;
import com.acidjobs.acidjobs.core.api.user.profile.contact.UserContact;
import com.acidjobs.acidjobs.core.api.user.profile.contact.UserPhoneRepository;
import com.acidjobs.acidjobs.core.api.user.resume.Resume;
import com.acidjobs.acidjobs.core.api.user.resume.ResumeRepository;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccount;
import com.acidjobs.acidjobs.core.api.user.socail_account.SocialAccountRepository;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class CompleteProfileController {
	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserPhoneRepository userPhoneRepository;

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

   @GetMapping("/get-complete-profile")
	public GenericResponse get(HttpServletRequest httpServletRequest) throws GenericException {
	   Set<String> pendingAction=new HashSet<>();
	   int strength=0;

	   String authorization = httpServletRequest.getHeader("Authorization");
	   User user= jwtUtility.getUserFromToken(authorization);
      // checking personal details
	   if(checkPersonalDetails(user)){strength+=15;}
	   else{pendingAction.add("Add Personal Details");}
	   //Checking Resume
	   if(checkResume(user)){strength+=15;}
	   else{pendingAction.add("Add Resume");}

      //checking graduation

	   if(checkGraduation(user)){
		   strength+=10;
	   }
	   else{
		   pendingAction.add("Add graduation details");
	   }

	   if(checkBasicEducation(user)){
		   strength+=10;
	   }
	   else{
		   pendingAction.add("Add High & Higher education");
	   }

	   if(checkSkill(user)){
		   strength+=15;
	   }
	   else{
		   pendingAction.add("Add IT skills");
	   }
	   if(checkSocialAccount(user)){
		   strength+=10;
	   }
	   else{
		   pendingAction.add("Add social account");
	   }

	   if(checkProjects(user)){
		   strength+=10;
	   }
	   else{
		   pendingAction.add("Add Project");
	   }
       
	   if(checkPhoneNumber(user)){
		   strength+=15;
	   }
	   else{
		   pendingAction.add("Add Phone Number");
	   }
	   CompleteProfileResponse completeProfileResponse=new CompleteProfileResponse(strength,pendingAction,false);
	   if(strength==100){
		   completeProfileResponse.setComplete(true);
		   return new GenericResponse("success","data found",completeProfileResponse);
	   }

       return new GenericResponse("success","data found",completeProfileResponse);


   }

	private boolean checkPhoneNumber(User user) {
		UserContact userContact=userPhoneRepository.findByUser(user);
		if(userContact!=null){
			return true;
		}
		return false;
	}

	private boolean checkProjects(User user) {
	   List<Project> projects=projectRepository.findByUser(user);
	   if(!projects.isEmpty()){
		   return true;
	   }
	   return false;
	}

	private boolean checkSocialAccount(User user) {
	   List<SocialAccount> socialAccounts=socialAccountRepository.findByUser(user);
	   if(!socialAccounts.isEmpty()){
		   return true;
	   }
	   return false;
	}

	private boolean checkSkill(User user) {
		List<Skills> skills=skillsRepository.findByUser(user);
		if(!skills.isEmpty()){
			return true;
		}
		return false;
	}

	private boolean checkBasicEducation(User user) {
		BasicEducation basicEducation=basicEducationRepository.findByUserAndQualification(user,"10th");
		BasicEducation basicEducation1=basicEducationRepository.findByUserAndQualification(user,"12th");
		BasicEducation basicEducation2=basicEducationRepository.findByUserAndQualification(user,"diploma");
		if(((basicEducation != null) && (basicEducation1 != null)) || ((basicEducation != null) && (basicEducation2 != null))){
			return true;
		}
		return false;
	}

	private boolean checkGraduation(User user) {
		Graduation graduation = graduationRepository.findByUserAndQualification(user,"ug");
		if(graduation!=null){
			return true;
		}
		return false;
	}

	private boolean checkPersonalDetails(User user) {
		PersonalDetails personalDetails=personalDetailsRepository.findByUser(user);
		if(personalDetails!=null){
			return true;
		}
		return false;
	}
	private boolean checkResume(User user) {
		Resume resume=resumeRepository.findByUser(user);
		if(resume!=null){
			return true;
		}
		return false;
	}

}
