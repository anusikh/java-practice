package com.acidjobs.acidjobs.core.email.service;

import javax.mail.MessagingException;

import com.acidjobs.acidjobs.core.email.context.AbstractEmailContext;

public interface EmailService {

	void sendMail(final AbstractEmailContext email) throws MessagingException;
}
