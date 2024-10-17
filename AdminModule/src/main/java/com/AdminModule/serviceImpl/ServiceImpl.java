package com.AdminModule.serviceImpl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.AdminModule.model.Customer;
import com.AdminModule.model.Enquiry;
import com.AdminModule.model.User;
import com.AdminModule.repository.AdminRepository;
import com.AdminModule.repository.CustomerRepository;
import com.AdminModule.repository.EnquiryRepository;
import com.AdminModule.repository.UserRepository;
import com.AdminModule.serviceI.ServiceI;

@Service
public class ServiceImpl implements ServiceI{

	@Autowired
	AdminRepository ar;
	
	@Autowired
	UserRepository us;
	
	@Autowired
	EnquiryRepository er;
	
	@Autowired
	CustomerRepository cr;
	
	@Autowired
    JavaMailSender sender; 
	
	public static String generateRandomChars (String candidateChars, int length) {
       StringBuilder sb = new StringBuilder ();
       Random random = new Random ();
       for (int i = 0; i < length; i ++) {
           sb.append (candidateChars.charAt (random.nextInt (candidateChars
                   .length ())));
       }

       return sb.toString ();
   }
	

	@Override
	public void addUsers(User u) {
		
			String password=generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890",6);
			u.setUsername(u.getEmail());
			u.setPassword(password);
			
			SimpleMailMessage message=new SimpleMailMessage();
				
			message.setTo(u.getEmail()); 
				
			message.setSubject("Bajaj Finance HomeLoan Application Access Details");
				
			message.setText("Hello,"+u.getEmpName()+",\nI hope you’re doing well.\n\n I’m pleased to confirm that you have been assigned the position of "+u.getAccountType()+", I am providing you with your login credentials as follows:\n your USERNAME : "
			+u.getUsername()+"\n your PASSWORD : "+u.getPassword()
			+"\n\nPlease ensure you change your password after your first login to maintain security."
			+ "As part of your role, you will be responsible for responsibilities related to "+u.getAccountType()+". If you have any questions or need further clarification about your duties, please let me know."
			+ "\n\nThank you, and Welcome to the Bajaj Finance Team, and best of luck in your new role!"
			+"\n\n\nBest regards,\nBajaj Finance Admin \nContact:8784788896");
				
			sender.send(message);
			
			ar.save(u);
	}


	@Override
	public List<Enquiry> getAllEnquiries() {
		
		return er.findAll();
	}


	@Override
	public List<Enquiry> getEnquiriesByEnquiryStatus(String eStatus) {
		
		return er.getByEnquiryStatus(eStatus);
	}


	@Override
	public List<Customer> getAllCustomers() {
		
		return cr.findAll();
	}


	@Override
	public List<Customer> getCustomersByEnquiryStatus(String eStatus) {
		
		return cr.getCustomersByEnquiryStatus(eStatus);
	}


	@Override
	public List<Customer> getCustomerByEnquiryStatusAndVerifiStatus(String eStatus, String vStatus) {
		
		return cr.getCustomerByEnquiryStatusAndVerifiStatus(eStatus,vStatus);
	}


	@Override
	public List<User> getAllUsers() {
		
		return ar.findAll();
	}


	@Override
	public List<User> getUsersByAccoType(String accountType) {
		
		return ar.getByAccountType(accountType);
	}


	@Override
	public List<Customer> getCustomerByLoanStatus(String loanStatus) {
		
		return cr.getCustomerByLoanStatus(loanStatus);
	}


	@Override
	public User getUserByUserAndPass(String unm, String pass) {
		
		return us.getByUsernameAndPassword(unm, pass);
	}
}
