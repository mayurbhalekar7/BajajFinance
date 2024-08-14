package com.OperationalExecutiveModule.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OperationalExecutiveModule.exception.CustomerNotFoundException;
import com.OperationalExecutiveModule.exception.EnquiryNotFountException;
import com.OperationalExecutiveModule.model.Customer;
import com.OperationalExecutiveModule.model.Enquiry;
import com.OperationalExecutiveModule.serviceI.OEServiceI;


@RestController
public class OEController {

	@Autowired
	OEServiceI oei;
	
	@GetMapping("/getAllEnquiries")
	public List<Enquiry> getAllEnquiries()
	{
		List<Enquiry> elist=oei.getAllEnquiries();
		return elist;
	}
	
	
	@PostMapping("/calculateCibilScore/{enquiryId}")
	public  ResponseEntity<String> calculateCibilScore(@PathVariable("enquiryId") int enquiryId)
	{
		Optional<Enquiry> op=oei.checkCibilScore(enquiryId);
		if(op.isPresent())
		{
			Enquiry e=op.get();
			if(e.getCibil().getCibilScore()>0)
			{
				ResponseEntity<String> re=new ResponseEntity<String>("send mail successfuly....",HttpStatus.OK);
				return re;
			}
			else
			{
				ResponseEntity<String> re=new ResponseEntity<String>("CibilScore Already Calculated....",HttpStatus.OK);
				return re;
			}
		}
		else
			throw new EnquiryNotFountException("Enquiry not found...");	
	}
	
	/*@PutMapping("/forwordToRelationalExecutive")
	public ResponseEntity<String> forwordToRelationalExecutive(@RequestBody Enquiry e)
	{
	    Optional<Enquiry> op=oei.getByEnquiryId(e.getEnquiryId());
	    ResponseEntity<String> re = null;
	    if(op.isPresent())
	    {
	    	Enquiry eq=op.get();
	    	if(eq.getEnquiryStatus().equals("f2oe") && eq.getCibil().getCibilStatus().equals("good"))
	    	{
		    	eq.setEnquiryStatus("f2re");
		    	oei.forwordToRelationalExecutive(eq);
		    	re=new ResponseEntity<String>("Enquiry forworded to Relational Executive and send the mail...",HttpStatus.ACCEPTED);
		    }
	    	return re;
	    }
	    else
	    	throw new EnquiryNotFountException("Enquiry not found...");		
	}*/
	
	@GetMapping("/forwordToRelationalExecutive/{email}")
	public ResponseEntity<String> forwordToRelationalExecutive(@PathVariable("email") String email)
	{
	    Optional<Enquiry> op=oei.getByEmail(email);
	    ResponseEntity<String> re = null;
	    if(op.isPresent())
	    {
	    	Enquiry eq=op.get();
	    	if(eq.getEnquiryStatus().equals("f2oe") && eq.getCibil().getCibilStatus().equals("good"))
	    	{
		    	eq.setEnquiryStatus("f2re");
		    	oei.forwordToRelationalExecutive(eq);
		    	re=new ResponseEntity<String>("Enquiry forworded to Relational Executive and send the mail...",HttpStatus.ACCEPTED);
		    }
	    	if(eq.getEnquiryStatus().equals("f2re"))
	    	{
	    	  re=new ResponseEntity<String>("Enquiry already forworded to Relational Executive...",HttpStatus.ACCEPTED);
	    	}
	    	return re;
	    }
	    else
	    	throw new EnquiryNotFountException("Enquiry not found...");		
	}
	
	
	/*@GetMapping("/getByF2oeAndGoodEnquiry")
	public List<Enquiry> getByF2oeAndGoodEnquiry()
	{
	   	List<Enquiry> elist=oei.getEnquiry();
	   	List<Enquiry> rlist=new ArrayList<>(); 
	   	for(Enquiry e:elist)
	   	{
	   		if(e.getEnquiryStatus().equals("f2oe") && e.getCibil().getCibilStatus().equals("good"))
	   	    rlist.add(e);
	   	}
	   	return rlist;
	}*/
	
	@GetMapping("/getByF2oeAndGoodEnquiry")
	public List<Enquiry> getByF2oeAndGoodEnquiry() {
		List<Enquiry> elist = oei.getEnquiry();
		return elist;
	}

	@GetMapping("/getAllVerifiPendingCustomers")
	public List<Customer> getCustomer() 
	{
		List<Customer> clist=oei.getCustomer();
		return clist;
	}
	
	
	@GetMapping("documentVerification/{customerId}")
	public ResponseEntity<String> documentVerification(@PathVariable("customerId") int cId)
	{
		Optional<Customer> op=oei.getCustomerByID(cId);
	    if(op.isPresent())
	    {
	    	Customer c=op.get();
	    	if(c.getVerification().getStatus().equals("pending"))
	    	{
	    		oei.forDocumentVerification(c);
	    		ResponseEntity<String> re=new ResponseEntity<String>("Documents Verified successfully..",HttpStatus.OK);
		    	return re;
	    	}
	    	else
		    {
		    	ResponseEntity<String> re=new ResponseEntity<String>("Documents are already proceded..",HttpStatus.OK);
		    	return re;
		    }
	    }
	    else
	    {
	    	throw new CustomerNotFoundException("Customer Not Peresent..");
	    }
	   
	}
	
	
	@PostMapping("customersforwordToCM/{customerId}")
	public ResponseEntity<String> customersforwordToCM(@PathVariable("customerId") int cid)
	{
		Optional<Customer> op=oei.getCustomerByID(cid);
	    if(op.isPresent())
	    {
	    	Customer c=op.get();
	    	if(c.getVerification().getStatus().equals("Verified"))
	    	{
	    		oei.forwordedToCM(c);
		    	ResponseEntity<String> re=new ResponseEntity<String>("Enquiry forworded to CM and send mail successfully..",HttpStatus.OK);
		    	return re;
	    	}
	    	else
	    	{
	    		ResponseEntity<String> re=new ResponseEntity<String>("Documents are not valid..",HttpStatus.OK);
		    	return re;
	    	}
	    }
	    else
	    {
	    	throw new CustomerNotFoundException("Customer Not Peresent..");
	    }
	}
	
}
