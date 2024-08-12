package com.OperationalExecutiveModule.exception;

public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException(String msg)
	{
		super(msg);
	}
}
