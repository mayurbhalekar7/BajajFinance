package com.OperationalExecutiveModule.serviceImp;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.OperationalExecutiveModule.model.Customer;
import com.OperationalExecutiveModule.model.Enquiry;
import com.OperationalExecutiveModule.repository.CustomerRepository;
import com.OperationalExecutiveModule.repository.OERepository;
import com.OperationalExecutiveModule.serviceI.OEServiceI;

@Service
public class OEServiceImpl implements OEServiceI {

	@Autowired
	OERepository or;
	
	@Autowired
	CustomerRepository cr;
	
	@Autowired
	JavaMailSender sender;

	@Override
	public List<Enquiry> getAllEnquiries() {

		List<Enquiry> elist= or.getByEnquiryStatus("f2oe");
		return elist;
	}

	@Override
	public Optional<Enquiry> checkCibilScore(int enquiryId) {
		
		Optional<Enquiry> op=or.getByEnquiryIdAndEnquiryStatus(enquiryId,"f2oe");
		if(op.isPresent())
		{
			Enquiry eq=op.get();
			if(eq.getCibil().getCibilScore()==0)
		      {
					Random random=new Random();
					int min=300,max=900,cibil=0;
					
					cibil=random.nextInt((max-min)+1)+min;
					
					eq.getCibil().setCibilScore(cibil);
					eq.getCibil().setCibilDate(new Date());
					
					SimpleMailMessage message=new SimpleMailMessage();
					message.setTo(eq.getEmail());
					message.setSubject("Bajaj Finance Home Loan Enquiry Status");
					if(cibil>650)
					{
						  eq.getCibil().setCibilStatus("good");
						  eq.setLoanStatus("pending");
						  message.setText("Hello,"+eq.getFirstName()+" "+eq.getLastName()+",\nYour CibilScore calculated by Bajaj Finance Team is " 
						  +eq.getCibil().getCibilScore()+", so your cibil status is good and your Loan Status is pending."+
						   "\n\nImportant documents for Loan:\n"
						   + "1.Loan application form\n"
						   + "2. 3 photographs passport sized\n"
						   + "3.Identify proof\n"
						   + "4.Residence Proof (Any One Required)\n"
						   + "  Electricity Bill\n  Ration Card\n  Telephone Bill\n  Employment Letter\n  Passbook or Bank Statement with address\n"
						   + "5.Bank Account Statement/Passbook for last 6 \n months\n"
						   + "6.Property detailed documents\n"
						   + "7.Salary Certificate (original) from employer\n"
						   + "8.Form 16/IT Returns for the past 2 financial years\n"
						   + "9.Proof of business address for non-salaried individuals.(Self-employed professionals)\n"
						   + "10.IT Returns/Assessment Orders copies of the last 3 years.(Self-employed professionals)\n"
						   + "\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
					}
					else
						{
						  eq.getCibil().setCibilStatus("not good");
						  eq.setLoanStatus("rejected");
						  message.setText("Hello,"+eq.getFirstName()+" "+eq.getLastName()+",\nYour CibilScore calculated by Bajaj Finance Team is " 
						  +eq.getCibil().getCibilScore()+", so your cibil status is not good and your Loan Status is rejected."+
						  "\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
						}
					sender.send(message);
					or.save(eq);
					return op;
		      }
		  else
			{
				eq.getCibil().setCibilScore(0);
				return op;
			}
		}
		else
		{
			return op;
		}
		
	}

	@Override
	public Optional<Enquiry> getByEmail(String email) {
		
		Optional<Enquiry> op=or.getByEmail(email);
		return op;
	}

	@Override
	public void forwordToRelationalExecutive(Enquiry eq) {
		
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(eq.getEmail());
		message.setSubject("Bajaj Finance Home Loan Enquiry Status");
		message.setText("Hello,"+eq.getFirstName()+" "+eq.getLastName()+",\nYour Home Loan Enquiry Status is forworded to Relational Manager."+
		"we will update you soon."+
		"\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
		
		sender.send(message);
		or.save(eq);	
	}

	@Override
	public List<Enquiry> getEnquiry() {
		
		return or.getByEnquiryStatusAndCibilStatus("f2oe","good");
	}

	@Override
	public List<Customer> getCustomer() {

		return cr.getByVerifiStatusPendingCustomer("pending");
	}

	@Override
	public Optional<Customer> getCustomerByID(int cid) {
		
		Optional<Customer> op=cr.findById(cid);
		return op;
	}

	@Override
	public void forDocumentVerification(Customer c) {
		
		c.getVerification().setStatus("Verified");
		cr.save(c);
	}

	@Override
	public void forwordedToCM(Customer c) {
		
		c.getEnquiry().setEnquiryStatus("f2cm");
		
		SimpleMailMessage message=new SimpleMailMessage();
		Enquiry eq=c.getEnquiry();
		message.setTo(c.getEnquiry().getEmail());
		message.setSubject("Bajaj Finance Home Loan Enquiry Status");
		message.setText("Hello,"+eq.getFirstName()+" "+eq.getLastName()+",\nYour Home Loan Enquiry Status is forworded to Credential Manager."+
		"we will update you soon."+
		"\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
		
		sender.send(message);
		cr.save(c);
	}
	
	
}
