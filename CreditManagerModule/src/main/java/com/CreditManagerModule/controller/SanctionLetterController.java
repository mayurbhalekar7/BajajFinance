package com.CreditManagerModule.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.CreditManagerModule.exception.ObjectNotFoundException;
import com.CreditManagerModule.model.Customer;
import com.CreditManagerModule.model.Enquiry;
import com.CreditManagerModule.model.SanctionLetter;
import com.CreditManagerModule.servicei.SanctionLetterServiceI;

@RestController
public class SanctionLetterController {

	@Autowired
	SanctionLetterServiceI sli;
	
	@PutMapping("saveSanctionLetterData/{customerId}")
	public ResponseEntity<String> SaveSanctionLetter(@PathVariable("customerId") int customerId,@RequestBody SanctionLetter sl)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if(customer.isPresent())
		{
			SanctionLetter sanctionLetter= customer.get().getSanctionLetter();
			if(sanctionLetter.getCalculatedEMI()==0 && customer.get().getEnquiry().getEnquiryStatus().equals("f2cm"))
			{
				sl.setSanctionId(sanctionLetter.getSanctionId());
				sl.setApplicantName(customer.get().getEnquiry().getFirstName()+" "+customer.get().getEnquiry().getLastName());
				sl.setSanctionLetterPDF(sanctionLetter.getSanctionLetterPDF());
				sl.setCalculatedEMI(0);
				sl.setSanctionLetterStatus("pending");
				sl.setSanctionDate(null);
				sli.updateSanctionLetterData(sl);
				ResponseEntity<String> rs=new ResponseEntity<String>("Sanction Letter Data Successfully updated...",HttpStatus.CREATED);
				return rs;
			}
			else
			{
				ResponseEntity<String> rs=new ResponseEntity<String>("Sanction Letter Data not updated because EMI Already Calculated or Customer not under Credit manager...",HttpStatus.ALREADY_REPORTED);
				return rs;
			}
		}
		else
		{
			throw new ObjectNotFoundException("Customer Not Found");
		}
	}
	
	@GetMapping("getSanctionLetterData/{customerId}")
	public ResponseEntity<SanctionLetter> getSanctionLetterData(@PathVariable("customerId") int customerId)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent())
		{
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			return new ResponseEntity<SanctionLetter>(sanctionLetter, HttpStatus.FOUND);		
		}
		else {
			throw new ObjectNotFoundException("Customer not found");
		}
	}
	
	@PutMapping("calculateEMI/{customerId}")
    public ResponseEntity<String> calculateEMI(@PathVariable("customerId") int customerId)
    {
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent()) 
		{
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			if(sanctionLetter.getSanctionAmount()!=0 && sanctionLetter.getInterestRate()!=0 && sanctionLetter.getTennure()!=0)
			{
				sli.calculateEMI(sanctionLetter);
				ResponseEntity<String> rs=new ResponseEntity<String>("EMI Successfully Calculated...",HttpStatus.OK);
				return rs;
			}
			else
			{
				ResponseEntity<String> rs=new ResponseEntity<String>("Sanction Amount,Interest Rate and Tennure are not decided yet...",HttpStatus.OK);
				return rs;
			}
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
    }
	
	@GetMapping("/getAllCustomersIsF2CMAndSanctionStatusNull")
	public ResponseEntity<List<Customer>> getAllCustomersIsF2CMAndSanctionStatusNull()
	{
		List<Customer> clist= sli.getAllCustomersIsF2CMAndSanctionStatusNull();
		if(clist.size()>0)
		{
			ResponseEntity<List<Customer>> rs=new ResponseEntity<List<Customer>>(clist,HttpStatus.FOUND);
			return rs;
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
	}
	
	@PutMapping("/generateSanctionLetterPDF/{customerId}")
	public ResponseEntity<String> generateSanctionLetterPDF(@PathVariable("customerId")int customerId)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent()) 
		{
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			if(sanctionLetter.getCalculatedEMI()>0)
			{	
				sli.generateSanctionLetterPDF(sanctionLetter);
				HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_PDF);
		        headers.setContentDispositionFormData("Attachment","Sanction Letter of "+sanctionLetter.getApplicantName()+" "+new Date()+".pdf");
				ResponseEntity<String> rs=new ResponseEntity<String>("Sanction Letter Generated Successfully...",HttpStatus.OK);
				return rs;
			}
			else
			{
				ResponseEntity<String> rs=new ResponseEntity<String>("Sanction Letter not able to generate...",HttpStatus.OK);
				return rs;
			}
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
	}
	
	@GetMapping("/forwordToAccountHead/{customerId}")
	public ResponseEntity<String> forwordToAccountHead(@PathVariable("customerId") int customerId)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent()) 
		{
			Enquiry enquiry = customer.get().getEnquiry();
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			if(enquiry.getEnquiryStatus().equals("f2cm") && sanctionLetter.getSanctionLetterStatus().equals("accept"))
			{
				enquiry.setEnquiryStatus("f2ah");
				sli.forwordToAccountHead(enquiry);
				ResponseEntity<String> re=new ResponseEntity<String>("Enquiry forworded to Account Head and send the mail...",HttpStatus.ACCEPTED);
				return re;
	    	}
			else
			{
				ResponseEntity<String> re=new ResponseEntity<String>("Enquiry not under Credit manager or Sanction Letter not accepted by Customer...",HttpStatus.NOT_FOUND);
				return re;
			}
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
	}
	
	@GetMapping("/forwardSanctionLetterToCustomerByEmail/{customerId}")
	public ResponseEntity<String> forwardSanctionLetterToCustomerByEmail(@PathVariable("customerId") int customerId)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent()) 
		{
			Enquiry enquiry = customer.get().getEnquiry();
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			if(enquiry.getEnquiryStatus().equals("f2cm") && (sanctionLetter.getSanctionLetterPDF() != null  && sanctionLetter.getSanctionLetterPDF().length > 0))
	    	{
				sli.forwardSanctionLetterToCustomer(customer.get(),enquiry,sanctionLetter);
				ResponseEntity<String> re=new ResponseEntity<String>("Sanction Letter send to customer successfully...",HttpStatus.OK);
				return re;
	    	}
			else
			{
				ResponseEntity<String> re=new ResponseEntity<String>("Sanction Letter not generated yet...",HttpStatus.NOT_FOUND);
				return re;
			}
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
	}
	
	@GetMapping("/getAllEnquriesByf2cm")
	public ResponseEntity<List<Enquiry>> getAllEnquriesByf2cm()
	{
		Optional<List<Enquiry>> elist= sli.getAllEnquriesByf2cm();
		if(elist.get().size()>0)
		{
			ResponseEntity<List<Enquiry>> re=new ResponseEntity<List<Enquiry>>(elist.get(),HttpStatus.OK);
			return re;
		}
		else
		{
			throw new ObjectNotFoundException("Enquiries not found...");
		}
	}
	
	@GetMapping("/getAllCustomersBySantionLetterStatus/{status}")
	public ResponseEntity<List<Customer>> getAllCustomersBySantionLetterStatus(@PathVariable("status") String status)
	{
		Optional<List<Customer>> clist= sli.getAllCustomersBySantionLetterStatus(status);
		if(clist.get().size()>0)
		{
			ResponseEntity<List<Customer>> re=new ResponseEntity<List<Customer>>(clist.get(),HttpStatus.OK);
			return re;
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found...");
		}
	}
}
     
