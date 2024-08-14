package com.OperationalExecutiveModule.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Disbursment {

	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int DisbursmentId;
	private double sanctionAmount;
	private Date date;
	private int tanuareInMonth;
	private  String disbursmentStaus;
	private String bankName;
	private String brnchName;
	private double tarnsferAmount;
	private String ifscCode;
	private Date tillDate;
}
