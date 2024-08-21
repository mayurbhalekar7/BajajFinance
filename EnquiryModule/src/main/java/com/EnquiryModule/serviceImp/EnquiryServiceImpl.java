package com.EnquiryModule.serviceImp;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.EnquiryModule.exception.EmailAlreadyExistException;
import com.EnquiryModule.model.Enquiry;
import com.EnquiryModule.repository.CibilRepository;
import com.EnquiryModule.repository.EnquiryRepository;
import com.EnquiryModule.serviceI.EnquiryServiceI;

import jakarta.transaction.Transactional;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI {

	@Autowired
	EnquiryRepository er;
	
	@Autowired
	CibilRepository cr;
	
	@Autowired
	private JavaMailSender sender; 
	
	@Override
	public void addEnquiry(Enquiry eq) {
		
		SimpleMailMessage message=new SimpleMailMessage();
		
		Optional<Enquiry> eo=er.findByEmail(eq.getEmail());
		if(eo.isPresent())
		{
			
			throw new EmailAlreadyExistException("Email already exist..."); 
		}
		else
		{
			message.setTo(eq.getEmail());
			
			message.setSubject("Bajaj Finance Enquiry Status");
			
			message.setText("Hello,"+eq.getFirstName()+" "+eq.getLastName()+"\n Your Enquiry received to Bajaj Finance, we will update you for your enquiry status.\n\n\n Thanks & Regards,\n Bajaj Finance Team");
			
			sender.send(message);
			
			er.save(eq);
		}
	}

	@Override
	public List<Enquiry> getAllEnquiries(String enquiryStatus) {
		return er.findAllByEnquiryStatus(enquiryStatus);
	}

	@Override
	public Optional<Enquiry> getEnquriryById(int enquiryId) {
		 Optional<Enquiry> enq=er.findByEnquiryIdAndEnquiryStatus(enquiryId,"pending");
		 return enq;
	}

	@Override
	public void updateEnquiry(Enquiry eq) {
		er.save(eq);
	}

	@Override
	public void deleteEnquiry(int eid) {
		er.deleteById(eid);
		cr.deleteById(eid);
	}

	@Override
	public Optional<List<Enquiry>> getEnquiryByLoanStatus(String loanStatus) {
		Optional<List<Enquiry>> enq=er.findAllByLoanStatus(loanStatus);
		return enq;
	}

	@Transactional
	@Override
	public void enquiryForwardToOE(Enquiry enquiry) {
		SimpleMailMessage message=new SimpleMailMessage();
		
		message.setTo(enquiry.getEmail());
		
		message.setSubject("Bajaj Finance Enquiry Status");
		
		message.setText("Hello,"+enquiry.getFirstName()+" "+enquiry.getLastName()+"\n Your Enquiry forward to Operational Executive in Bajaj Finance, we will update you soon for next process.\n\n\n Thanks & Regards,\n Bajaj Finance Team");
		
		sender.send(message);
		
		er.save(enquiry);
	}

	@Override
	public Optional<Enquiry> getEnquiryByEmail(String email) {
		Optional<Enquiry> enq=er.findByEmailAndEnquiryStatus(email,"pending");
		return enq;
	}

	@Override
	public List<Enquiry> getAllEnquiriesBySorting() {
		return er.findAll();
	}
}
