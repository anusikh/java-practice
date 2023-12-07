package com.acidjobs.acidjobs.core.user.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserInfo {
	private String email;
	private String firstName;
	private String lastName;
	private String role;
}
