package com.acidjobs.acidjobs.core.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.service.UserService;
import com.acidjobs.acidjobs.exception.InvalidTokenException;
import com.acidjobs.acidjobs.exception.UserAlreadyExistException;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;

@RequestMapping("/register")
@CrossOrigin
@RestController
public class RegistrationController {
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/create-user")
	public GenericResponse userRegistration(@RequestBody User user) throws UserAlreadyExistException {

		try {
			var encode = passwordEncoder.encode(user.getPassword());
			user.setPassword(encode);
			user.setRole("USER");
			userService.register(user);
			return new GenericResponse("success", "Account is created", null);
		} catch (UserAlreadyExistException ex) {
			var message = ex.getMessage();
			return new GenericResponse("error", message, null);
		}

	}

	@GetMapping("/verify")
	public String verifyCustomer(@RequestParam(required = false) String token, final Model model, RedirectAttributes redirAttr){
		if(StringUtils.isEmpty(token)){
		   return "token is empty";
		}
		try {
			userService.verifyUser(token);
			return "Account verified successfully";
		} catch (InvalidTokenException e) {
			return "token is invalid";
		}


	}

}