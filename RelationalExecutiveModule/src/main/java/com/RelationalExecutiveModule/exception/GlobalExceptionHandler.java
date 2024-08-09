package com.RelationalExecutiveModule.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=CustomerNotFoundException.class)
	public ResponseEntity<ApiError> CustomerNotFoundExceptionHandler(HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMassage("Customer Not Found");
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler 
	public ResponseEntity<ApiError> EnquiryNotFoundExceptionHandler(EnquiryNotFoundException e,HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMassage("Enquiry  Not Found");
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
}
