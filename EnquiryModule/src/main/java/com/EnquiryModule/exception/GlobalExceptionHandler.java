package com.EnquiryModule.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=EnquiryNotFoundException.class)
	public ResponseEntity<ApiError> EnquiryNotFoundExceptionHandler(HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMassage("Enquiry Not Found");
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = EmailAlreadyExistException.class)
	public ResponseEntity<ApiError>PancardAlreadyExistException(HttpServletRequest request)
	{
		ApiError error=new ApiError();
		error.setMassage("Email Already Exist");
		error.setPath(request.getRequestURI());
		error.setStatusCode(HttpStatus.NOT_FOUND.value());
		error.setStatusMessage(HttpStatus.NOT_FOUND);
		error.setTimeStamp(new Date());
		return new ResponseEntity<ApiError>(error,HttpStatus.NOT_FOUND);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) 
	{
		Map<String, String> errorMap = new HashMap<>();
	    ex.getBindingResult().getFieldErrors().forEach
	    (
	    	error -> 
	    	{
	    	errorMap.put(error.getField(), error.getDefaultMessage());
	    	}
	    );
	    return errorMap;
	}
	
	/*@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EnquiryNotFoundException.class)
    public Map<String, String> handleBusinessException(EnquiryNotFoundException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errorMessage", ex.getMessage());
        return errorMap;
    }*/
}
