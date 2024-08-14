package com.CreditManagerModule.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
@Entity
public class Enquiry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int enquiryId;
	@NotEmpty(message = "Please enter first name.")
	private String firstName;
	
	@NotEmpty(message = "Please enter last name.")
	private String lastName;
	
	@Min(18)
	@Max(60)
    private int age;
	
    @Email
	private String email;
	
    @Pattern(regexp = "^\\d{10}$",message = "Please enter 10 digit mobile number.")
	private String mobileNo;
	
	@NotEmpty(message = "Please enter aadhar card number.")
	private String adharcard;
	 
	@NotEmpty(message = "Please enter pan card number.")
	private String pancard;
	
	
	private String gender;
	
	private String enquiryStatus;
	
	private String loanStatus;
	 
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibil;

}
