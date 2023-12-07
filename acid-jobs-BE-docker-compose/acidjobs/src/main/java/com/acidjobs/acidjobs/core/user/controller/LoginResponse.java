package com.acidjobs.acidjobs.core.user.controller;

import com.acidjobs.acidjobs.pojo.JwtResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
	private JwtResponse jwtResponse;
	private UserInfo userInfo;


}
