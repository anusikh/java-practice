package com.acidjobs.acidjobs.core.api.user.personalDetails;

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

import com.acidjobs.acidjobs.core.api.user.employment.Employment;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class PersonalDetailsController {
	  @Autowired
	private PersonalDetailsService personalDetailsService;
	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/save-personal-details")
	private PersonalDetails  save(@RequestBody PersonalDetails personalDetails, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		personalDetails.setUser(user);
		return personalDetailsService.save(personalDetails);
	}

	@GetMapping("/personal-details")
	private PersonalDetails get( HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return personalDetailsService.get(user);
	}

}
