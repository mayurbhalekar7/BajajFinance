package com.CreditManagerModule.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FamilyInformation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int familyInformationId;
	private int familyMember;
	private int childerns;
	private String maritalSatus;
	private int dependentMember;
	private double familyIncome;
	

}