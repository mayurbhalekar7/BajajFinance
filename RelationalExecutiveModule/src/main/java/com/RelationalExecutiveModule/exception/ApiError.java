package com.RelationalExecutiveModule.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//data Transfer Object used for data Transfer Known as DTO

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
 
	private int statusCode;
	private String massage;
	private HttpStatus statusMessage;
	private Date timeStamp;
	private String path;
}
