package com.acidjobs.acidjobs.core.api.user.employment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class EmploymentController {
	@Autowired
	private EmploymentService employmentService;
    @Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/save-employment")
	private List<Employment> save(@RequestBody Employment employment, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		employment.setUser(user);
		return employmentService.save(employment);
	}

	@PostMapping("/update-employment")
	private List<Employment> update(@RequestBody Employment employment, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		employment.setUser(user);
		return employmentService.update(employment);
	}

	@PostMapping("/delete-employment")
	private List<Employment> delete(@RequestParam("id") Long id, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);

		return employmentService.delete(id,user);
	}
	@GetMapping("/get-employment")
	private List<Employment> get( HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return employmentService.get(user);
	}
}
