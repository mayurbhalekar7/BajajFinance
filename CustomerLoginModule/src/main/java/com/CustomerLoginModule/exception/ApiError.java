package com.CustomerLoginModule.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
	
	private int statusCode;
	private String massage;
	private HttpStatus statusMessage;
	private Date timeStamp;
	private String path;

}
