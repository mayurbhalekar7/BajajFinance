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
	
	@PutMapping("saveSanctionLetter/{customerId}")
	public ResponseEntity<String> SaveSanctionLetter(@PathVariable("customerId") int customerId,@RequestBody SanctionLetter sl)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if(customer.isPresent())
		{
			int sanctionId = customer.get().getSanctionLetter().getSanctionId();
			sl.setSanctionId(sanctionId);
			sli.updateSanctionLetterData(sl);
			
			ResponseEntity<String> rs=new ResponseEntity<String>("Sanction Letter Data Successfully updated...",HttpStatus.CREATED);
			return rs;
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

		if (customer.isPresent()) {
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
		if (customer.isPresent()) {
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			sli.calculateEMI(sanctionLetter);
			ResponseEntity<String> rs=new ResponseEntity<String>("EMI Successfully Calculated...",HttpStatus.OK);
			return rs;
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
    }
	
	@GetMapping("/getAllPendingSanctions")
	public ResponseEntity<List<SanctionLetter>> getAllPendingSanctions()
	{
		List<SanctionLetter> sanctionLetters= sli.getAllPendingSanctions();
		ResponseEntity<List<SanctionLetter>> rs=new ResponseEntity<List<SanctionLetter>>(sanctionLetters,HttpStatus.FOUND);
		return rs;
	}
	
	@PutMapping("/generateSanctionLetterPDF/{customerId}")
	public ResponseEntity<byte[]> generateSanctionLetterPDF(@PathVariable("customerId")int customerId)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent()) {
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			byte[] sanctionLetterPDF=sli.generateSanctionLetterPDF(sanctionLetter);
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDispositionFormData("Attachment","Sanction Letter of "+sanctionLetter.getApplicantName()+" "+new Date()+".pdf");
			ResponseEntity<byte[]> rs=new ResponseEntity<byte[]>(sanctionLetterPDF,headers,HttpStatus.OK);
			return rs;
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
			//if(enquiry.getEnquiryStatus().equals("f2cm") && sanctionLetter.getSanctionLetterStatus().equals("accept"))
			if(enquiry.getEnquiryStatus().equals("Registered") && sanctionLetter.getSanctionLetterStatus().equals("pending"))	
	    	{
				enquiry.setEnquiryStatus("f2ah");
				sli.forwordToAccountHead(enquiry);
				ResponseEntity<String> re=new ResponseEntity<String>("Enquiry forworded to Account Head and send the mail...",HttpStatus.ACCEPTED);
				return re;
	    	}
			else
			{
				ResponseEntity<String> re=new ResponseEntity<String>("Enquiry not in Credit manager state or Sanction Letter not accepted by Customer...",HttpStatus.NOT_FOUND);
				return re;
			}
		}
		else
		{
			throw new ObjectNotFoundException("Customer not found");
		}
	}
	
	@GetMapping("/forwardSanctionLetterToCustomer/{customerId}")
	public ResponseEntity<String> forwardSanctionLetterToCustomer(@PathVariable("customerId") int customerId)
	{
		Optional<Customer> customer = sli.getCustomerById(customerId);
		if (customer.isPresent()) 
		{
			Enquiry enquiry = customer.get().getEnquiry();
			SanctionLetter sanctionLetter = customer.get().getSanctionLetter();
			//if(enquiry.getEnquiryStatus().equals("f2cm") && sanctionLetter.getSanctionLetterStatus().equals("accept"))
			if(enquiry.getEnquiryStatus().equals("Registered") && sanctionLetter.getSanctionLetterStatus().equals("pending"))
	    	{
				sli.forwardSanctionLetterToCustomer(customer.get(),enquiry,sanctionLetter);
				ResponseEntity<String> re=new ResponseEntity<String>("Sanction Letter send to customer successfully...",HttpStatus.NOT_FOUND);
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
		Optional<List<Enquiry>> enquiry= sli.getAllEnquriesByf2cm();
		ResponseEntity<List<Enquiry>> re=new ResponseEntity<List<Enquiry>>(enquiry.get(),HttpStatus.OK);
		return re;
		
	}
}
     
