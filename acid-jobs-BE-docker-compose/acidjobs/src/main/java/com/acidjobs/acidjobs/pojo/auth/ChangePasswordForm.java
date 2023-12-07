package com.acidjobs.acidjobs.pojo.auth;

import lombok.Data;

@Data
public class ChangePasswordForm {
	private String oldPassword;
	private String newPassword;
}
