package com.acidjobs.acidjobs.core.api.user.socail_account;

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

import com.acidjobs.acidjobs.core.api.user.Skill.Skills;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class SocialAccountController {
	@Autowired
	private SocialAccountService socialAccountService;
	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/save-social-account")
	public List<SocialAccount> save(@RequestBody SocialAccount socialAccount, HttpServletRequest httpServletRequest) throws GenericException {

		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		socialAccount.setUser(user);
		return socialAccountService.save(socialAccount);
	}

	@PostMapping("/update-social-account")
	public List<SocialAccount> update(@RequestBody SocialAccount socialAccount, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		socialAccount.setUser(user);
		return socialAccountService.update(socialAccount);
	}

	@PostMapping("/delete-social-account")
	public List<SocialAccount> delete(@RequestParam("accountName") String accountName, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return socialAccountService.delete(accountName,user);
	}

	@GetMapping("/get-social-account")
	public List<SocialAccount> get(HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
		return socialAccountService.get(user);
	}
}
