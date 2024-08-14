package com.AccountHeadModule.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class Verification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int verificationId;
	private Date verificationDate;
	private String status;
	private String remark;

}
