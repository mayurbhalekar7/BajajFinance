package com.AccountHeadModule.exception;

public class LedgerNotFoundException extends RuntimeException {
	
	public LedgerNotFoundException(String msg)
	{
		super(msg);
	}

}
