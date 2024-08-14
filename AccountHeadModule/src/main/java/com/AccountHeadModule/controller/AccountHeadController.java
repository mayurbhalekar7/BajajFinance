package com.AccountHeadModule.controller;


import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.AccountHeadModule.exception.CustomerNotFoundException;
import com.AccountHeadModule.exception.DisbursmentNotFoundException;
import com.AccountHeadModule.exception.LedgerNotFoundException;
import com.AccountHeadModule.model.Customer;
import com.AccountHeadModule.model.Disbursment;
import com.AccountHeadModule.model.Ledger;
import com.AccountHeadModule.serviceI.ServiceI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AccountHeadController {
	
	@Autowired
	ServiceI si;
	
	@PutMapping("/addDisbusrment/{customerId}")
	public ResponseEntity<String> addDisbusrmentById(@PathVariable("customerId")int customerId ,@RequestBody Disbursment disbursment) {
		Optional<Customer> op=si.findById(customerId);
		
		if(op.isPresent())
		{
			
			si.saveDisbursment(customerId, disbursment);
			ResponseEntity<String> rs = new ResponseEntity<String>("Disbursment Added Succesfully.",HttpStatus.OK);
			return rs;
		}
		else {
			throw  new CustomerNotFoundException("Invalid customer  id");
		}
		
		
	}
	
	 @PostMapping("/sendDisbursment/{customerId}")
	 public  ResponseEntity<String> sendDisbursment(@PathVariable("customerId") int customerId)
	 {
		 Optional<Customer> op=si.findById(customerId);
			
			if(op.isPresent())
			{
				int DisbursmentId=op.get().getDisbursment().getDisbursmentId();
				si.sendDisbursment(DisbursmentId);
				ResponseEntity<String> rs = new ResponseEntity<String>("Lone Amount Added Successfully",HttpStatus.OK);
				return rs;
			}
			else {
				throw new CustomerNotFoundException("Invalid customer  id");
			}
		 
	 }

	 @PostMapping("createLedger/{customerId}")
	 public  ResponseEntity<String> createLedger(@PathVariable("customerId")int customerId) {
		 Optional<Customer> op=si.findById(customerId);
		 if(op.isPresent())
		 {
		    si.createLedger(customerId); 
			 
	 	return  new ResponseEntity<String>("Lerger created for "+customerId,HttpStatus.OK);
		 }
		 else
		 {
				throw new CustomerNotFoundException("Invalid customer  id");
			}
	 }
	 
	   @PostMapping("/payEmi/{customerId}/{ledgerId}")
	   public ResponseEntity<String> payEmi(@PathVariable("customerId")int customerId,@PathVariable("ledgerId")int ledgerId)
	   {
		   Optional<Customer> op=si.findById(customerId);
			 if(op.isPresent())
			 {
				 Customer cus=op.get();
				 if(cus.getDisbursment().getDisbursmentStaus().equals("done"))
				  
				 {
					
					String s=si.updateLedger(customerId,ledgerId);
					ResponseEntity<String> rs = new ResponseEntity<String>(s,HttpStatus.OK);
					return rs;
				 }
				 
				 else {
					 throw new LedgerNotFoundException("Invalid ledger  id");
				 }
			 }
			 else
			 {
					throw new CustomerNotFoundException("Invalid customer  id");
				} 
	   }
	   
	   @PostMapping("/skipEmi/{customerId}/{ledgerId}")
	   public ResponseEntity<String> skipEmi(@PathVariable("customerId")int customerId,@PathVariable("ledgerId")int ledgerId)
	   {
		   Optional<Customer> op=si.findById(customerId);
			 if(op.isPresent())
			 {
				 Customer cus=op.get();
				 List<Ledger> l= cus.getLedeger();
				 String s= si.skipEmi(l,ledgerId);
				 ResponseEntity<String> rs = new ResponseEntity<String>(s,HttpStatus.OK);
				 return rs;
			 }
			 else
			 {
					throw new CustomerNotFoundException("Invalid customer  id");
				}
		
	   }
	    @GetMapping("/getAllSantionLaterAccepted")
	    public ResponseEntity<List<Customer>> getAllSantionLaterAccepted()
	    {
	    	List <Customer> li=si.getAllSantionLaterAccepted();
	    	ResponseEntity<List<Customer>> rs = new ResponseEntity<List<Customer>>(li,HttpStatus.OK);
			 return rs;
	    	
	    }
	   }
	   

