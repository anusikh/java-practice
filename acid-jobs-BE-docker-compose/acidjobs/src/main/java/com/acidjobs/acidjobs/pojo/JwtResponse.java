package com.acidjobs.acidjobs.pojo;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String accessToken;
	private String refreshToken;
}