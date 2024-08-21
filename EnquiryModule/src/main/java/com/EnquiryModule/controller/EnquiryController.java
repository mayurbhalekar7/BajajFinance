package com.EnquiryModule.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import jakarta.validation.Valid;


@RestController
public class EnquiryController {

	@Autowired
	EnquiryServiceI enqs;

	private static Logger log = LoggerFactory.getLogger(EnquiryController.class);

	@PostMapping("/addEnquiry")
	public ResponseEntity<String> addEnquiry(@Valid @RequestBody Enquiry eq)
	{ 
		eq.setEnquiryStatus("pending");
		eq.setLoanStatus("pending");
		eq.getCibil().setCibilStatus("pending");
		eq.getCibil().setCibilDate(null);
		eq.getCibil().setCibilScore(0);
		enqs.addEnquiry(eq);
		ResponseEntity<String> rs=new ResponseEntity<String>("Send your Enquiry successfully...",HttpStatus.CREATED);
		return rs;
	}

	@GetMapping("/getAllPendingEnquiries")
	public ResponseEntity<List<Enquiry>> getAllEnquiry()
	{
		log.info("Getting all enquiry");
		List<Enquiry> enqList = enqs.getAllEnquiries("pending");
		ResponseEntity<List<Enquiry>> elist=new ResponseEntity<List<Enquiry>>(enqList, HttpStatus.OK);
		return elist;
	}

	@GetMapping("/getEnquiryByEmail/{email}")
	public ResponseEntity<Enquiry> getEnquiryByEmail(@PathVariable("email") String email)
	{
		Optional<Enquiry> enq=enqs.getEnquiryByEmail(email);
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
			enqs.updateEnquiry(eq);
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
			throw new EnquiryNotFoundException("Not a single Enquiry found...");
		}
	}
	
/*	@PutMapping("/enquiryForwardToOE")
	public ResponseEntity<String> enquiryForwardToOE(@RequestBody Enquiry eq)
	{
		Optional<Enquiry> enq=enqs.getEnquriryById(eq.getEnquiryId());
		if(enq.isPresent())
		{
			if(enq.get().getEnquiryStatus().equals("pending"))
			{
				eq.setEnquiryStatus("f2oe");
				enqs.enquiryForwardToOE(eq);
				ResponseEntity<String> rs=new ResponseEntity<String>("Enquiry Forward to Operational Executive...",HttpStatus.OK);
				return rs;
			}
			else
			{
				ResponseEntity<String> rs=new ResponseEntity<String>("Enquiry Already Forwarded...",HttpStatus.ALREADY_REPORTED);
				return rs;
			}
		}
		else
		{
			throw new EnquiryNotFoundException("Invalid Enquiry Id");
		}
	}*/
	@GetMapping("/enquiryForwardToOE/{email}")
	public ResponseEntity<String> enquiryForwardToOE(@PathVariable("email") String email) 
	{
		Optional<Enquiry> enq=enqs.getEnquiryByEmail(email);
		//if(enq.get().getEnquiryStatus().equals("pending"))
		if(enq.isPresent())
		{
			enq.get().setEnquiryStatus("f2oe");
			enqs.enquiryForwardToOE(enq.get());
			ResponseEntity<String> rs=new ResponseEntity<String>("Enquiry Forward to Operational Executive...",HttpStatus.OK);
			return rs;
		}
		else
		{
			throw new EnquiryNotFoundException("Invalid Enquiry email or status is not pending");
		}
	}
	
	@GetMapping("/getAllEnquiriesBySorting/{choice}")
	   public List<Enquiry> getAllEnquiriesBySorting(@PathVariable("choice") int choice)
	   {
		   List<Enquiry> elist=enqs.getAllEnquiriesBySorting();
		   if(choice==1)
		   {
			   List<Enquiry> rlisr=elist.stream().sorted(Comparator.comparing(Enquiry::getFirstName)).collect(Collectors.toList());
			   return rlisr;
		   }
		   else if(choice==2)
		   {
			   List<Enquiry> rlisr=elist.stream().sorted(Comparator.comparing(Enquiry::getLastName)).collect(Collectors.toList());
			   return rlisr;
		   }
		   else if(choice==3)
		   {
			   List<Enquiry> rlisr=elist.stream().sorted(Comparator.comparing(Enquiry::getAge)).collect(Collectors.toList());
			   return rlisr;
		   }
		   else
			   return elist;
	   }
}
