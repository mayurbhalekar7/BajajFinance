package com.AccountHeadModule.serviceI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.AccountHeadModule.model.Customer;
import com.AccountHeadModule.model.Disbursment;
import com.AccountHeadModule.model.Ledger;

public interface ServiceI {

	public Optional<Customer> findById(int disbursmentId);

	 public void saveDisbursment( int customerId,Disbursment disbursment);

	public void sendDisbursment(int disbursmentId);

	public void createLedger(int customerId);

	public String updateLedger( int customerId , int ledgerId);

	public String skipEmi(List<Ledger> l, int ledgerId);

	public List<Customer> getAllSantionLaterAccepted();

}
