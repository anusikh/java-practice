package com.acidjobs.acidjobs.core.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.user.jpa.data.CustomUser;
import com.acidjobs.acidjobs.core.user.jpa.data.RefreshToken;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.service.RefreshTokenService;
import com.acidjobs.acidjobs.core.user.service.UserService;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.JwtRequest;
import com.acidjobs.acidjobs.pojo.JwtResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
public class LoginController {

	@Autowired
	private JWTUtility jwtUtility;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/authenticate")
	public LoginResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getEmail(),
							jwtRequest.getPassword()
					)
			);
		}
		catch (DisabledException ex){
            throw new GenericException("Your account is not verify please verified");
		}

		catch (BadCredentialsException e) {
			throw new GenericException("Invalid Credentials");
		}


		final User user=userService.getUserByUsername(jwtRequest.getEmail());

		CustomUser customUser=new CustomUser(user);
		final String token =
				jwtUtility.generateToken(customUser);
		RefreshToken refreshToken=refreshTokenService.createRefreshToken(customUser.getUsername());
      UserInfo userInfo=new UserInfo(user.getEmail(),user.getFirstName(), user.getLastName(), user.getRole());
	  JwtResponse jwtResponse=new JwtResponse(token,refreshToken.getToken());
		return new LoginResponse(jwtResponse, userInfo);
	}


	@GetMapping("/user/logout")
	public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) throws Exception{
		String authorization = httpServletRequest.getHeader("Authorization");
		User user= jwtUtility.getUserFromToken(authorization);
        int delete=refreshTokenService.deleteByUserId(user);
		if(delete>0){
			return ResponseEntity.ok("Logout successfully");
		}
        return ResponseEntity.badRequest().body("No logged in user find");
	}
}
