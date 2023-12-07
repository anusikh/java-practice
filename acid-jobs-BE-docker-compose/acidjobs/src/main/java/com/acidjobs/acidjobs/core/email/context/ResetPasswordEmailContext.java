package com.acidjobs.acidjobs.core.email.context;

import org.springframework.web.util.UriComponentsBuilder;

import com.acidjobs.acidjobs.core.user.jpa.data.User;

public class ResetPasswordEmailContext extends AbstractEmailContext{
	private String token;
	@Override
	public <T> void init(T context){
		User user = (User) context;
		put("firstName", user.getFirstName());
		setTemplateLocation("emails/reset-password");
		setSubject("Reset your password");
		setFrom("no-reply@acidjobs.com");
		setTo(user.getEmail());
	}

	public void setToken(String token) {
		this.token = token;
		put("token", token);
	}

	public void buildVerificationUrl(final String baseURL, final String token){
		final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
											  .path("/reset-password").queryParam("token", token).toUriString();
		put("resetPasswordURL", url);
	}
}
