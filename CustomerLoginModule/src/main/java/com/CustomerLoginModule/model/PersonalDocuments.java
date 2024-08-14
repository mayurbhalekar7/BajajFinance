package com.CustomerLoginModule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PersonalDocuments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int documentId;
	@Column(length = 999999999)
	private byte[] adharcard;
	@Column(length = 999999999)
	private byte[] pancard;
	@Column(length = 999999999)
	private byte[] photo;
	@Column(length = 999999999)
	private byte[] signature;
	@Column(length = 999999999)
	private byte[] OccupationProof;
	@Column(length = 999999999)
	private byte[] ITreturn;
	@Column(length = 999999999)
	private byte[] addressProof;
	

}