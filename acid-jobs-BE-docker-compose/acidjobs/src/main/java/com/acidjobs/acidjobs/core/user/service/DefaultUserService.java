package com.acidjobs.acidjobs.core.user.service;

import java.util.Objects;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import com.acidjobs.acidjobs.core.email.context.AccountVerificationEmailContext;
import com.acidjobs.acidjobs.core.email.context.ResetPasswordEmailContext;
import com.acidjobs.acidjobs.core.email.service.EmailService;
import com.acidjobs.acidjobs.core.user.jpa.data.SecureToken;
import com.acidjobs.acidjobs.core.user.jpa.data.User;
import com.acidjobs.acidjobs.core.user.repository.SecureTokenRepository;
import com.acidjobs.acidjobs.core.user.repository.UserRepository;
import com.acidjobs.acidjobs.exception.InvalidTokenException;
import com.acidjobs.acidjobs.exception.UserAlreadyExistException;
import com.acidjobs.acidjobs.pojo.auth.ResetPasswordForm;

@Service
public class DefaultUserService implements UserService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SecureTokenService secureTokenService;

	@Autowired
	SecureTokenRepository secureTokenRepository;



	@Value("${site.base.url.https}")
	private String baseURL;

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}

	@Override
	public void register(User user) throws UserAlreadyExistException {
		if(checkIfUserExist(user.getEmail())){
			throw new UserAlreadyExistException("User already exists for this email address");
		}
		userRepository.save(user);
		sendRegistrationConfirmationEmail(user);

	}



	@Override
	public boolean checkIfUserExist(String email) {
		return userRepository.findByEmail(email)!=null ? true : false;
	}



	@Override
	public void sendRegistrationConfirmationEmail(User user) {
		SecureToken secureToken= secureTokenService.createSecureToken();
		secureToken.setUser(user);
		secureTokenRepository.save(secureToken);
		AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
		emailContext.init(user);
		emailContext.setToken(secureToken.getToken());
		emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
		try {
			emailService.sendMail(emailContext);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean verifyUser(String token) throws InvalidTokenException {
		SecureToken secureToken = secureTokenService.findByToken(token);
		if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
			throw new InvalidTokenException("Token is not valid");
		}
		User user = userRepository.getById(secureToken.getUser().getId());
		if(user==null){
			return false;
		}
		user.setAccountVerified(true);
		userRepository.save(user); // let's same user details

		// we don't need invalid password now
		secureTokenService.removeToken(secureToken);
		return true;
	}

	@Override
	public User getUserById(String id)  {
		User user= userRepository.findByEmail(id);

		return user;
	}

	@Override
	public boolean updatePassword(User user, String newPassword) {
		User user1=userRepository.findByEmail(user.getEmail());
	    if(user1!=null){

			user1.setPassword(newPassword);
			userRepository.save(user1);
			return true;
		}
		return false;
	}


	@Override
	public boolean sendResetPasswordEmail(User user) {
		SecureToken secureToken= secureTokenService.createSecureToken();
		secureToken.setUser(user);
		secureTokenRepository.save(secureToken);
		ResetPasswordEmailContext emailContext = new ResetPasswordEmailContext();
		emailContext.init(user);
		emailContext.setToken(secureToken.getToken());
		emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
		try {
			emailService.sendMail(emailContext);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
       return false;
	}

	@Override
	public boolean verifyResetPassword(ResetPasswordForm resetPasswordForm) {
		SecureToken secureToken = secureTokenService.findByToken(resetPasswordForm.getToken());
		if(Objects.isNull(secureToken) || !StringUtils.equals(resetPasswordForm.getToken(), secureToken.getToken()) || secureToken.isExpired()){
		  return false;
		}
		User user = userRepository.getById(secureToken.getUser().getId());
		if(user!=null) {
			updatePassword(user,resetPasswordForm.getPassword());
			secureTokenService.removeToken(secureToken);
			return true;
		}
		return false;
	}

}
