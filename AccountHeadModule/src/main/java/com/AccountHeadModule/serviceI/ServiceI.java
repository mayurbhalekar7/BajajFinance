package com.AccountHeadModule.serviceI;

import java.util.List;
import java.util.Optional;

import com.AccountHeadModule.model.Customer;
import com.AccountHeadModule.model.Disbursment;
import com.AccountHeadModule.model.Ledger;

public interface ServiceI {

	public Optional<Customer> findById(int disbursmentId);

	 public void saveDisbursment( int customerId,Disbursment disbursment);

	public void sendDisbursment(Customer customer, int disbursmentId);

	public String createLedger(int customerId);

	public String updateLedger( int customerId , int ledgerId);

	public String skipEmi(List<Ledger> l, int ledgerId,Customer customer);

	public List<Customer> getAllSantionLaterAccepted();
	
	public boolean CheckCustomerAndLedgerMapping(int customerId,int ledgerId);

}
