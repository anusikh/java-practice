package com.acidjobs.acidjobs.core.user.service;

import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.exception.InvalidTokenException;
import com.acidjobs.acidjobs.exception.UserAlreadyExistException;
import com.acidjobs.acidjobs.pojo.auth.ResetPasswordForm;

public interface UserService {
    User getUserByUsername(String username);
	void register(final User user) throws UserAlreadyExistException;
	boolean checkIfUserExist(final String email);
	void sendRegistrationConfirmationEmail(final User user);
	boolean verifyUser(final String token) throws InvalidTokenException;
	User getUserById(final String id);
	boolean updatePassword(User user, String newPassword);
	boolean sendResetPasswordEmail(final User user);

	boolean verifyResetPassword(ResetPasswordForm resetPasswordForm);
}
