package com.acidjobs.acidjobs.exception;

public class ContactAlreadyExists  extends Exception{
	public ContactAlreadyExists() {
		super();
	}

	public ContactAlreadyExists(String message) {
		super(message);
	}

	public ContactAlreadyExists(String message, Throwable cause) {
		super(message, cause);
	}

	public ContactAlreadyExists(Throwable cause) {
		super(cause);
	}

	protected ContactAlreadyExists(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
