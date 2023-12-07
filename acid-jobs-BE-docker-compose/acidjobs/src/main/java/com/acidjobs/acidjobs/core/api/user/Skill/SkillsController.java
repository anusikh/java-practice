package com.acidjobs.acidjobs.core.api.user.Skill;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class SkillsController {

	@Autowired
	private SkillsService skillsService;

	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/save-skills")
	public List<Skills> save(@RequestBody Skills skills, HttpServletRequest httpServletRequest) throws GenericException {

		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		skills.setUser(user);
		return skillsService.save(skills);
	}

	@PostMapping("/update-skills")
	public List<Skills> update(@RequestBody Skills skills, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		skills.setUser(user);
		return skillsService.update(skills);
	}

	@PostMapping("/delete-skills")
	public List<Skills> delete(@RequestParam("skillName") String skillName, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return skillsService.delete(skillName,user);
	}

	@GetMapping("/get-skills")
	public List<Skills> get(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return skillsService.get(user);
	}
}
