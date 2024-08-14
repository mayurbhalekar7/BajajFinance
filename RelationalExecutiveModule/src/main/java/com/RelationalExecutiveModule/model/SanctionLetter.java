package com.RelationalExecutiveModule.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SanctionLetter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sanctionId;
	
	private String applicantName;
	
	private Date sanctionDate;
	
	private long sanctionAmount;
	
	private int tennure;
	
	private float interestRate;
	
	private double calculatedEMI;
	
	private String sanctionLetterStatus;
	
	@Column(length = 999999999)
	private byte[] sanctionLetterPDF;
}