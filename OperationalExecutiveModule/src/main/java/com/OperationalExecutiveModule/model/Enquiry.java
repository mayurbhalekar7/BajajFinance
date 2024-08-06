package com.OperationalExecutiveModule.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Enquiry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int enquiryId;
	
	private String firstName;
	
	private String lastName;
	
    private int age;
	
	private String email;
	
	private long mobileNo;
	
	private String adharcard;
	 
	private String pancard;
	
	private String gender;
	
	private String enquiryStatus;
	
	private String loanStatus;
	 
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibil;

}
