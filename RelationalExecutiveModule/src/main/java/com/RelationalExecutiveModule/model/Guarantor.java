package com.RelationalExecutiveModule.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Guarantor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int guarantorId;

	private String guarantorName;
	private String guarantorAdharcarNo;
	private String relationship;
	private String guarantorAddress;
	private String guarantorOccupation;
	private String guarantorAccountNo;

}
