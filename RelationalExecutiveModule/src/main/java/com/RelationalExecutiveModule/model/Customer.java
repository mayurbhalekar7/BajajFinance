package com.RelationalExecutiveModule.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerId;
	private String username;
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	private Enquiry enquiry;
	
	@OneToOne(cascade = CascadeType.ALL)
	private FamilyInformation family;
	
	@OneToOne(cascade = CascadeType.ALL)
	private BankAccount account;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Verification verification;

	@OneToOne(cascade = CascadeType.ALL)
	private PersonalDocuments documents;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address address;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Guarantor guarantor;

}
