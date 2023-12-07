package com.acidjobs.acidjobs.core.email.context;


import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

public class AccountVerificationEmailContext extends AbstractEmailContext {

	private String token;


	@Override
	public <T> void init(T context){
		//we can do any common configuration setup here
		// like setting up some base URL and context
		User user = (User) context; // we pass the customer informati
		put("firstName", user.getFirstName());
		setTemplateLocation("emails/email-verification");
		setSubject("Complete your registration");
		setFrom("no-reply@acidjobs.com");
		setTo(user.getEmail());
	}

	public void setToken(String token) {
		this.token = token;
		put("token", token);
	}

	public void buildVerificationUrl(final String baseURL, final String token){
		final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
											  .path("/register/verify").queryParam("token", token).toUriString();
		put("verificationURL", url);
	}
}
