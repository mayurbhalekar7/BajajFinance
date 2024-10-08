package com.CustomerLoginModule.serviceimpl;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.CustomerLoginModule.model.Address;
import com.CustomerLoginModule.model.BankAccount;
import com.CustomerLoginModule.model.Customer;
import com.CustomerLoginModule.model.Enquiry;
import com.CustomerLoginModule.model.PersonalDocuments;
import com.CustomerLoginModule.repository.AddressRepository;
import com.CustomerLoginModule.repository.BankAccountRepository;
import com.CustomerLoginModule.repository.CustomerRepository;
import com.CustomerLoginModule.repository.EnquiryRepository;
import com.CustomerLoginModule.repository.PersonalDocumentsRepository;
import com.CustomerLoginModule.servicei.ServiceI;

@Service
public class ServiceImpl implements ServiceI {
	
	@Autowired
	CustomerRepository cr;
	
	@Autowired
	BankAccountRepository br;
	
	@Autowired
	AddressRepository ar;
	
	@Autowired
	PersonalDocumentsRepository pr;
	
	@Autowired
	EnquiryRepository er;

	@Override
	public Optional<Customer> getCustomerByUsernameAndPassword(String username, String password) {
		
		Optional<Customer> op=cr.getByUsernameAndPassword(username,password);
		return op;
	}

	@Override
	public void updateCustomerByEmail(Customer cus) {
		
		cr.save(cus);
	}

	@Override
	public Customer findByUsername(String emailId) {
		Customer cust=cr.findByUsername(emailId).get();
		 return cust;
	}

	
	@Autowired
	JavaMailSender mailSender;

	    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    private static final int OTP_LENGTH = 6;

	    @Override
	    public String changePassword() {
	        SecureRandom random = new SecureRandom();
	        StringBuilder otp = new StringBuilder(OTP_LENGTH);
	        for (int i = 0; i < OTP_LENGTH; i++) {
	            otp.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
	        }
	        return otp.toString();
	    }
	
	    @Override
	    public void sendOTPToEmail(String toEmail, String otp) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(toEmail);
	        message.setSubject("Your OTP Code");
	        message.setText("Your OTP code is: " + otp);
	        mailSender.send(message);
	    }

		@Override
		public BankAccount getBankAccount(int customerId) {
			
			Customer cus =cr.findById(customerId).get();
			int id=cus.getAccount().getAccountId();
			BankAccount bk=br.findById(id).get();
			 
			return  bk;
		}

		@Override
		public Address getAddress(int customerId) {
			
			Customer cus=cr.findById(customerId).get();
			int id=cus.getAddress().getAddressId();
			Address as= ar.findById(id).get();
			
			return as;
		}

		@Override
		public PersonalDocuments getPersonalDocuments(int customerId) {
			
			Customer cus=cr.findById(customerId).get();
			int id= cus.getDocuments().getDocumentId();
			PersonalDocuments pd=pr.findById(id).get();
			return  pd;
		}

		@Override
		public Enquiry getEnquiry(int customerId) {
			Customer cust=cr.findById(customerId).get();
			int id=cust.getEnquiry().getEnquiryId();
			Enquiry se = er.findById(id).get();
;			return se;
		}

		@Override
		public Optional<Customer> findUsername(String username) {
			Optional<Customer> op=cr.findByUsername(username);
			return op;
		}

		@Override
		public void sendPasswordOnEmail(Customer customer) 
		{
			  String otp=changePassword();
			  customer.setPassword(otp);
			  SimpleMailMessage message = new SimpleMailMessage();
		      message.setTo(customer.getUsername());
		      message.setSubject("Bajaj Finance Coustomer Password");
		      message.setText(" Hello \n your current password is  : " + otp);
		      mailSender.send(message);
		      cr.save(customer);
		}

		@Override
		public void updateCustomer(Customer cusD) {
			cr.save(cusD);
			
		}

		@Override
		public void setnewPassword(Optional<Customer> customer, String newPassword) {
			customer.get().setPassword(newPassword);
			cr.save(customer.get());
		}
}
	
		
		
	

