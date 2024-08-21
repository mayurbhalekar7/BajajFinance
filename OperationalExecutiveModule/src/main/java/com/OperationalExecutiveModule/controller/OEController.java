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
import com.OperationalExecutiveModule.model.PersonalDocuments;
import com.OperationalExecutiveModule.serviceI.OEServiceI;


@RestController
public class OEController {

	@Autowired
	OEServiceI oei;
	
	@GetMapping("/getAllF2OEEnquiries")
	public ResponseEntity<List<Enquiry>> getAllEnquiries()
	{
		List<Enquiry> elist=oei.getAllEnquiries();
		if(elist.size()>0)
		{
			ResponseEntity<List<Enquiry>> re=new ResponseEntity<List<Enquiry>>(elist,HttpStatus.OK);
			return re;
		}
		else
		{
			throw new EnquiryNotFountException("Enquiries not found...");	
		}
	}
	
	
	@PostMapping("/calculateCibilScore/{enquiryId}")
	public  ResponseEntity<String> calculateCibilScore(@PathVariable("enquiryId") int enquiryId)
	{
		Optional<Enquiry> enquiry=oei.checkCibilScore(enquiryId);
		if(enquiry.isPresent())
		{
			if(enquiry.get().getCibil().getCibilScore()>0)
			{
				ResponseEntity<String> re=new ResponseEntity<String>("send mail successfuly....",HttpStatus.CREATED);
				return re;
			}
			else
			{
				ResponseEntity<String> re=new ResponseEntity<String>("CibilScore Already Calculated....",HttpStatus.OK);
				return re;
			}
		}
		else
		{
			throw new EnquiryNotFountException("Enquiry not found...");	
		}	
	}
	
	@GetMapping("/forwordEnquiryToRelationalExecutive/{email}")
	public ResponseEntity<String> forwordToRelationalExecutive(@PathVariable("email") String email)
	{
	    Optional<Enquiry> enquiry=oei.getByEmail(email);
	    ResponseEntity<String> re = null;
	    if(enquiry.isPresent())
	    {
	    	
	    	if(enquiry.get().getEnquiryStatus().equals("f2oe") && enquiry.get().getCibil().getCibilStatus().equals("good"))
	    	{
		    	enquiry.get().setEnquiryStatus("f2re");
		    	oei.forwordToRelationalExecutive(enquiry.get());
		    	re=new ResponseEntity<String>("Enquiry forworded to Relational Executive and send the mail...",HttpStatus.CREATED);
		    	return re;
		    }
	    	else
	    	{
	    	    re=new ResponseEntity<String>("Enquiry not under operational executive or cibil status are not good...",HttpStatus.NOT_FOUND);
	    	    return re;
	    	}
	    	
	    }
	    else
	    	throw new EnquiryNotFountException("Enquiry not found...");		
	}
	
	
	@GetMapping("/getByF2oeAndGoodEnquiry")
	public ResponseEntity<List<Enquiry>> getByF2oeAndGoodEnquiry() {
		List<Enquiry> elist = oei.getEnquiry();
		if(elist.size()>0)
		{
			ResponseEntity<List<Enquiry>> re=new ResponseEntity<List<Enquiry>>(elist,HttpStatus.OK);
			return re;
		}
		else
		{
			throw new EnquiryNotFountException("Enquiry not found...");	
		}
	}

	@GetMapping("/getAllVerifiPendingCustomers")
	public ResponseEntity<List<Customer>> getCustomer() 
	{
		List<Customer> clist=oei.getCustomer();
		if(clist.size()>0)
		{
			ResponseEntity<List<Customer>> re=new ResponseEntity<List<Customer>>(clist,HttpStatus.OK);
			return re;
		}
		else
		{
			throw new CustomerNotFoundException("Customer Not Peresent..");
		}
		
	}
	
	
	@GetMapping("documentVerification/{customerId}")
	public ResponseEntity<String> documentVerification(@PathVariable("customerId") int cId)
	{
		Optional<Customer> customer=oei.getCustomerByID(cId);
	    if(customer.isPresent())
	    {
	    	if(customer.get().getVerification().getStatus().equals("pending") && customer.get().getEnquiry().getEnquiryStatus().equals("f2oe"))
	    	{
	    		oei.forDocumentVerification(customer.get());
	    		ResponseEntity<String> re=new ResponseEntity<String>("Documents Verified successfully..",HttpStatus.OK);
		    	return re;
	    	}
	    	else
		    {
		    	ResponseEntity<String> re=new ResponseEntity<String>("Documents are already proceded or Enquiry not under operational executive..",HttpStatus.OK);
		    	return re;
		    }
	    }
	    else
	    {
	    	throw new CustomerNotFoundException("Customer Not Peresent..");
	    }
	   
	}
	
	
	@GetMapping("customersforwordToCM/{customerId}")
	public ResponseEntity<String> customersforwordToCM(@PathVariable("customerId") int customerId)
	{
		Optional<Customer> customer=oei.getCustomerByID(customerId);
	    if(customer.isPresent())
	    {
	    	if(customer.get().getVerification().getStatus().equals("Verified") && customer.get().getEnquiry().getEnquiryStatus().equals("f2oe"))
	    	{
	    		oei.forwordedToCM(customer.get());
		    	ResponseEntity<String> re=new ResponseEntity<String>("Enquiry forworded to Credit Manager and send mail successfully..",HttpStatus.OK);
		    	return re;
	    	}
	    	else
	    	{
	    		ResponseEntity<String> re=new ResponseEntity<String>("Documents are not valid or Enquiry not under Operational Executive..",HttpStatus.OK);
		    	return re;
	    	}
	    }
	    else
	    {
	    	throw new CustomerNotFoundException("Customer Not Peresent..");
	    }
	}
	
	
	@GetMapping("/viewPersonalDocuments/{customerId}")
	public ResponseEntity<PersonalDocuments> getPersonalDocuments(@PathVariable("customerId") int customerId) 
	{
		Optional<Customer> customer = oei.getCustomerByID(customerId);
		if (customer.isPresent() && customer.get().getEnquiry().getEnquiryStatus().equals("f2oe")) 
		{
			PersonalDocuments personalDocuments = oei.getPersonalDocuments(customerId);
			return new ResponseEntity<PersonalDocuments>(personalDocuments, HttpStatus.OK);
		} 
		else 
		{
			throw new CustomerNotFoundException("Customer Not Peresent..");
		}
	}
	
}
