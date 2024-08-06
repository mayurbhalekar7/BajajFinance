package com.EnquiryModule.serviceImp;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.EnquiryModule.exception.PancardAlreadyExistException;
import com.EnquiryModule.model.Enquiry;
import com.EnquiryModule.repository.EnquiryRepository;
import com.EnquiryModule.serviceI.EnquiryServiceI;

@Service
public class EnquiryServiceImpl implements EnquiryServiceI {

	@Autowired
	EnquiryRepository er;
	
	@Autowired
	private JavaMailSender sender; 
	
	int cibilScore=ThreadLocalRandom.current().nextInt(300, 900);
	
	@Override
	public void addEnquiry(Enquiry eq) {
    SimpleMailMessage message=new SimpleMailMessage();
		
		message.setTo(eq.getEmail());
		
		message.setSubject("Finvaege Enquiry Status");
		
		message.setText("Hello,"+eq.getFirstName()+" "+eq.getLastName()+"\n Your Enquiry received to Finverge Finance, we will update you for your enquiry status.\n\n\n Thanks & Regards,\n Finverge Team");
		
		sender.send(message);
		
		// List<Enquiry> elist=er.findAll();
		  Optional<Enquiry> eo=er.findByPancard(eq.getPancard());
		  
			if(eo.isPresent())
			{
				throw new PancardAlreadyExistException("Pancard already exist..."); 
			}
			else
			{
				er.save(eq);
			}
				
		 }

	@Override
	public List<Enquiry> getAllEnquiries() {

		return er.findAll();
	}

	@Override
	public Optional<Enquiry> getEnquriryById(int enquiryId) {
		 Optional<Enquiry> enq=er.findById( enquiryId);
			
		return enq;
	}

	@Override
	public void updateEnquiry(int eid, Enquiry eq) {

		er.save(eq);
	}


	@Override
	public void deleteEnquiry(int eid) {
		
		er.deleteById(eid);
	}

	@Override
	public Optional<List<Enquiry>> getEnquiryByLoanStatus(String loanStatus) {
		 Optional<List<Enquiry>> enq=er.findAllByLoanStatus(loanStatus);
		return enq;
	}

	@Override
	public void updateEnquiryStatus(int enquiryId, Enquiry eq) {
		er.save(eq);
		
	}


}
