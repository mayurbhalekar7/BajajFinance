package com.CreditManagerModule.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class Ledger
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ledgerId;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date ledgerCreatedDate;
	
	private double totalPrincipalAmount;
	
	private double payableAmount;
	
	private int tenure;
	
	private double monthlyEmi;
	
	private double amountPaidTillDate;
	
	private double remainingAmount;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date nextEmiStartDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date nextEmiEndDate;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)	
	private String previousEmiStatus;
	
	private String currentMonthEmiStatus;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)	
	private Date loanEndDate;
	
	private String loanStatus;
}
