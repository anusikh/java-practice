package com.acidjobs.acidjobs.core.api.user.Project;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.api.user.employment.Employment;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/save-project")
	private List<Project> save(@RequestBody Project project, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		project.setUser(user);
		return projectService.save(project);
	}

	@PostMapping("/update-project")
	private List<Project> update(@RequestBody Project project, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		project.setUser(user);
		return projectService.update(project);
	}

	@PostMapping("/delete-project")
	private List<Project> delete(@RequestParam("id") Long id, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return projectService.delete(id,user);
	}
	@GetMapping("/get-project")
	private List<Project> get( HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return projectService.get(user);
	}
}
