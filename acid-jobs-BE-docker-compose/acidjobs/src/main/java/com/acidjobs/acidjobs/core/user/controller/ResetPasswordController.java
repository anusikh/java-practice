package com.acidjobs.acidjobs.core.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.service.UserService;
import com.acidjobs.acidjobs.exception.InvalidTokenException;
import com.acidjobs.acidjobs.exception.UserAlreadyExistException;
import com.acidjobs.acidjobs.pojo.auth.EmailRequest;
import com.acidjobs.acidjobs.pojo.auth.ResetPasswordForm;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;

@RestController
@CrossOrigin
public class ResetPasswordController {
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	@PostMapping("/reset_password")
	public GenericResponse userRegistration(@RequestBody EmailRequest request){
		User user=userService.getUserByUsername(request.getEmail());
		if(user==null){
			return new GenericResponse("error","No account found with this email address",null);
		}
		boolean sentLink=userService.sendResetPasswordEmail(user);
		if(sentLink){
			return new GenericResponse("success","Reset password link sent your password",null);
		}
		return new GenericResponse("error","Something went wrong",null);
	}

	@PostMapping("/reset_password/verify")
	public GenericResponse verifyCustomer(@RequestBody ResetPasswordForm resetPasswordForm){
		if(StringUtils.isEmpty(resetPasswordForm.getToken())){
			return new GenericResponse("error", "token is empty",null);
		}
		resetPasswordForm.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword()));
		boolean resetPassword=userService.verifyResetPassword(resetPasswordForm);
		if(resetPassword){
			return new GenericResponse("success", "Account password reset  successfully",null);
		}
		return new GenericResponse("error", "token is invalid",null);
	}
}
