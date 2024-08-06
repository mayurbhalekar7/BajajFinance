package com.EnquiryModule.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.EnquiryModule.exception.EnquiryNotFoundException;
import com.EnquiryModule.model.Enquiry;
import com.EnquiryModule.serviceI.EnquiryServiceI;


@RestController
public class EnquiryController {
 
	@Autowired
	EnquiryServiceI enqs;
	
	private static Logger log = LoggerFactory.getLogger(EnquiryController.class);
	
	@GetMapping("/")
	public String preLogin()
	{
		return "This is enquiry module";
	}
	
	@PostMapping("/addEnquiry")
	public ResponseEntity<String> addEnquiry(@RequestBody Enquiry eq)
	{ 
		ResponseEntity<String> rs=new ResponseEntity<String>("Send your Enquiry successfully...",HttpStatus.CREATED);
		
		
			
		enqs.addEnquiry(eq);
		return rs;
		
	}
	
	@GetMapping("/getAllEnquiries")
	public ResponseEntity<List<Enquiry>> getAllEnquiry()
	{
		log.info("Getting all enquiry");
		List<Enquiry> enqList = enqs.getAllEnquiries();
		ResponseEntity<List<Enquiry>> elist=new ResponseEntity<List<Enquiry>>(enqList, HttpStatus.OK);
		return elist;
	}
	  
	@GetMapping("/getEnquriryById/{enquiryId}")
	public ResponseEntity<Enquiry> getEnquriryById(@PathVariable("enquiryId") int enquiryId)
	{
		Optional<Enquiry> enq=enqs.getEnquriryById(enquiryId);
		if(enq.isPresent())
			
		{
			return new ResponseEntity<>(enq.get(), HttpStatus.OK);
		}
		else
		{
			
			throw new EnquiryNotFoundException("Invalid Enquiry Id");
		}
		
	}
	

	@PutMapping("/updateEnquiry/{enquiryId}")
	public ResponseEntity<String> updateEnquiry(@PathVariable("enquiryId") int eid,@RequestBody Enquiry eq)
	{
		Optional<Enquiry> enq=enqs.getEnquriryById(eid);
		if(enq.isPresent())
		{
			enqs.updateEnquiry(eid,eq);
			ResponseEntity<String> rs=new ResponseEntity<String>("Your Enquiry Updated...",HttpStatus.OK);
			return rs;
		}
		else
		{
			throw new EnquiryNotFoundException("Invalid Enquiry Id");
		}
	}
	  @DeleteMapping("/deleteEnquiry/{enquiryId}") 
	public  ResponseEntity<String> deleteEnquiry(@PathVariable("enquiryId") int eid)
	{
		  Optional<Enquiry> enq=enqs.getEnquriryById(eid);
			if(enq.isPresent())
			{
				enqs.deleteEnquiry(eid);
				ResponseEntity<String> rs=new ResponseEntity<String>("Your Enquiry deleted...",HttpStatus.OK);
				return rs;
			}
			else
			{
				throw new EnquiryNotFoundException("Invalid Enquiry Id");
			}
	}
	  
	//Api written by mayur bhalekar 04-08-2024 for get loan status for getting loan status from enquirymodule
	  @GetMapping("/getEnquiryByLoanStatus/{loanStatus}")
		public ResponseEntity<List<Enquiry>> getEnquiryByLoanStatus(@PathVariable("loanStatus") String loanStatus)
		{
			Optional<List<Enquiry>> enq=enqs.getEnquiryByLoanStatus(loanStatus);
			if(enq.get().size()>0)
			{
				return new ResponseEntity<>(enq.get(), HttpStatus.OK);
			}
			else
			{
				throw new EnquiryNotFoundException("Not a single loan status found...");
			}
		}
	//Api written by mayur bhalekar 04-08-2024 to forward enquiry to Operational Manager
	  @PutMapping("/updateEnquiryStatus/{enquiryId}")
	  public ResponseEntity<String> updateEnquiryStatus(@PathVariable("enquiryId") int enquiryId,@RequestBody Enquiry eq)
	  {
		  Optional<Enquiry> enq=enqs.getEnquriryById(enquiryId);
			if(enq.isPresent())
			{
				eq.setEnquiryStatus("f2oe");
				enqs.updateEnquiryStatus(enquiryId,eq);
				ResponseEntity<String> rs=new ResponseEntity<String>("Enquiry Forward to Operational Manager...",HttpStatus.OK);
				return rs;
			}
			else
			{
				throw new EnquiryNotFoundException("Invalid Enquiry Id");
			}
	  }
}
