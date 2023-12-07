package com.acidjobs.acidjobs.core.user.controller;

import java.awt.geom.GeneralPath;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acidjobs.acidjobs.core.user.jpa.data.CustomUser;
import com.acidjobs.acidjobs.core.user.jpa.data.RefreshToken;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.service.RefreshTokenService;
import com.acidjobs.acidjobs.exception.GenericException;
import com.acidjobs.acidjobs.pojo.JwtResponse;
import com.acidjobs.acidjobs.pojo.response.GenericResponse;
import com.acidjobs.acidjobs.utility.JWTUtility;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class RefreshTokenController {


	@Autowired
	private JWTUtility jwtUtility;
	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/refreshtoken")
	public GenericResponse getRefreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) throws GenericException {

		String requestRefreshToken = tokenRefreshRequest.getRefreshToken();
		RefreshToken refreshToken=refreshTokenService.findByToken(requestRefreshToken);
		if(refreshToken!=null){
			RefreshToken refreshToken1=refreshTokenService.verifyExpiration(refreshToken);
			if(refreshToken1!=null){
				String token = jwtUtility.generateToken(new CustomUser(refreshToken.getUser()));
				JwtResponse jwtResponse=new JwtResponse(token, requestRefreshToken);
                return new  GenericResponse("success","",jwtResponse);

			}
			return new  GenericResponse("error",requestRefreshToken+ "Refresh token was expired. Please make a new signin request",null);


		}
		return new  GenericResponse("error","Refresh token is not in database!",null);



	}
}
