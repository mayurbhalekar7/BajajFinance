package com.RelationalExecutiveModule.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.RelationalExecutiveModule.model.Customer;
import com.RelationalExecutiveModule.model.Enquiry;
import com.RelationalExecutiveModule.repository.CustomerRepository;
import com.RelationalExecutiveModule.repository.EnquiryRepository;
import com.RelationalExecutiveModule.service.RelationalExecuteServiceI;

@Service
public class RelationlExecutiveServiceImpl  implements RelationalExecuteServiceI{

	@Autowired
	CustomerRepository cr;
	
	@Autowired
	EnquiryRepository er;
	
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
	
	String password=generateRandomChars("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890",6);

	@Override
	public void saveCustomer(Customer cusD) {
		
		cusD.setPassword(password);
		cusD.setUsername(cusD.getEnquiry().getEmail());
		
       SimpleMailMessage message=new SimpleMailMessage();
		
		message.setTo(cusD.getEnquiry().getEmail());
		
		message.setSubject("Bajaj Finance Loan Application Status");
		
		message.setText("Hello,"+cusD.getEnquiry().getFirstName()+" "+cusD.getEnquiry().getLastName()+"\n Your Loan Application received to Bajaj Finance, we will update you for your Loan status.\n your USERNAME : "+cusD.getUsername()+"\n your PASSWORD : "+cusD.getPassword()+"\n\n\n Thanks & Regards,\n Bajaj Finance Team");
		
		sender.send(message);
		cusD.getEnquiry().setEnquiryStatus("Registered");
		cusD.getVerification().setStatus("pending");
		cr.save(cusD);
		
	}

	@Override
	public Optional<Customer> getCustomerById(int customerId) {
		
		Optional<Customer> op=cr.findById(customerId);
		
		return op;
	}

	@Override
	public void updateCustomer(Customer cusD) {
		
		 
		cr.save(cusD);
		
	}

	@Override
	public List<Customer> getAllCutomer() {
		List<Customer> listc=(List<Customer>) cr.findAll();
		return listc;
	}

	@Override
	public Optional<Enquiry> findById(int id) {
		
		 Optional<Enquiry> eq=er.findById(id);
		return eq;
	}
}
