package com.CustomerLoginModule.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class BankAccount {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int accountId;
	private String accountHolderName;
	private String accountType;
	private String bankName;
	private String branchName;
	private String ifscCode;
	private long accountNo;
	private double accountBalance;

	

}