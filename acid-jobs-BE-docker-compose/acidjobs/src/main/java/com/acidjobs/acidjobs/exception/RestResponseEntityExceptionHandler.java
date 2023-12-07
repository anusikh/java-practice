package com.acidjobs.acidjobs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(InvalidTokenException.class)
	protected ResponseEntity<ErrorResponse> handleConflict(Exception ex) {
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	protected ResponseEntity<ErrorResponse> userNotFound(Exception ex) {
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(GenericException.class)
	protected ResponseEntity<ErrorResponse> genericException(Exception ex) {
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}
	@ExceptionHandler(ContactAlreadyExists.class)
	protected ResponseEntity<ErrorResponse> contactAlreadyExists(Exception ex) {
		ErrorResponse errorResponse=new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
	}
}
