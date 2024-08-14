package com.CustomerLoginModule.servicei;

import java.util.Optional;

import com.CustomerLoginModule.model.Address;
import com.CustomerLoginModule.model.BankAccount;
import com.CustomerLoginModule.model.Customer;
import com.CustomerLoginModule.model.Enquiry;
import com.CustomerLoginModule.model.PersonalDocuments;

public interface ServiceI {

	Optional<Customer> getCustomerByUsernameAndPassword(String username, String password);

	public void updateCustomerByEmail(Customer cus);

	public Customer findByUsername(String emailId);

	public String generateOtp();

	void sendOtpEmail(String toEmail, String otp);

    public Optional<Customer> getCustomerById(int customerId);

	public BankAccount getBankAccount(int customerId);

	public Address getAddress(int customerId);

	public PersonalDocuments getPersonalDocuments(int customerId);
	


	public Enquiry getEnquiry(int customerId);

	public Optional<Customer> findUsername(String emailId);

	public void sendOPtPasswod(Customer cus);

	public void updateCustomer(Customer cusD);
	
}
