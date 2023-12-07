package com.acidjobs.acidjobs.core.user.controller;

import javax.persistence.JoinColumn;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.service.UserService;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.auth.ChangePasswordForm;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class ChangePasswordController {
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTUtility jwtUtility;

	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordForm changePasswordForm, HttpServletRequest httpServletRequest) throws GenericException {
		String authorization = httpServletRequest.getHeader("Authorization");
        User user= jwtUtility.getUserFromToken(authorization);
		if(passwordEncoder.matches(changePasswordForm.getOldPassword(),user.getPassword())){
		 boolean is_updated=userService.updatePassword(user, passwordEncoder.encode(changePasswordForm.getNewPassword()));
		 if(is_updated){
			 return ResponseEntity.ok("Password updated successfully");
		 }

			 return ResponseEntity.ok("Password not updated please try again");

		}
       else{
		   throw new GenericException("Password did not matched");
		}
	}
}
