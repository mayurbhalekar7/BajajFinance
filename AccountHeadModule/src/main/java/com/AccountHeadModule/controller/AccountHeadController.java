package com.AccountHeadModule.controller;


import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.AccountHeadModule.exception.CustomerNotFoundException;
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
		Optional<Customer> customer=si.findById(customerId);
		if(customer.isPresent() && customer.get().getEnquiry().getEnquiryStatus().equals("f2ah") && !customer.get().getDisbursment().getDisbursmentStaus().equals("done"))
		{
			si.saveDisbursment(customerId, disbursment);
			ResponseEntity<String> rs = new ResponseEntity<String>("Disbursment Added Succesfully.",HttpStatus.OK);
			return rs;
		}
		else 
		{
			throw  new CustomerNotFoundException("Customer Not Found...");
		}
	}
	
	 @PutMapping("/sendDisbursmentToBankAccount/{customerId}")
	 public  ResponseEntity<String> sendDisbursment(@PathVariable("customerId") int customerId)
	 {
		 Optional<Customer> customer=si.findById(customerId);
		 if(customer.isPresent() && customer.get().getDisbursment().getDisbursmentStaus().equals("pending"))
		 {
		 	int DisbursmentId=customer.get().getDisbursment().getDisbursmentId();
			si.sendDisbursment(customer.get(),DisbursmentId);
			ResponseEntity<String> rs = new ResponseEntity<String>("Lone Amount Added Successfully to related bank account...",HttpStatus.OK);
			return rs;
		}
		else 
		{
			throw new CustomerNotFoundException("Customer Not Found...");
		} 
	 }

	 @PostMapping("createLedger/{customerId}")
	 public  ResponseEntity<String> createLedger(@PathVariable("customerId")int customerId) {
		 Optional<Customer> customer=si.findById(customerId);
		 if(customer.isPresent())
		 {
			String responseString =si.createLedger(customerId);  
			return  new ResponseEntity<String>(responseString+" Customer Id:"+customerId,HttpStatus.OK);
		 }
		 else
		 {
				throw new CustomerNotFoundException("Customer not found...");
		 }
	 }
	 
	   @PutMapping("/payEmi/{customerId}/{ledgerId}")
	   public ResponseEntity<String> payEmi(@PathVariable("customerId")int customerId,@PathVariable("ledgerId")int ledgerId)
	   {
		   Optional<Customer> customer=si.findById(customerId);
		   if(customer.isPresent())
		   {
			   if(si.CheckCustomerAndLedgerMapping(customerId, ledgerId)==false)
			   {
				   throw new LedgerNotFoundException("Ledger Not Found..."); 
			   }
			   if(customer.get().getDisbursment().getDisbursmentStaus().equals("done") )
			   {
					String responseString=si.updateLedger(customerId,ledgerId);
					ResponseEntity<String> rs = new ResponseEntity<String>(responseString,HttpStatus.OK);
					return rs;
				 }
				 else 
				 {
					 throw new LedgerNotFoundException("Ledger Not Found...");
				 }
			 }
			 else
			 {
					throw new CustomerNotFoundException("Customer Not Found...");
			 } 
	   }
	   
	   @PutMapping("/skipEmi/{customerId}/{ledgerId}")
	   public ResponseEntity<String> skipEmi(@PathVariable("customerId")int customerId,@PathVariable("ledgerId")int ledgerId)
	   {
		   	 Optional<Customer> customer=si.findById(customerId);
			 if(customer.isPresent())
			 {
				 if(si.CheckCustomerAndLedgerMapping(customerId, ledgerId)==false)
				 {
				    throw new LedgerNotFoundException("Ledger Not Found..."); 
				 }
				 if(customer.get().getLedeger().size()>1)
				 {
					 List<Ledger> ledgerList= customer.get().getLedeger();
					 String s= si.skipEmi(ledgerList,ledgerId,customer.get());
					 ResponseEntity<String> rs = new ResponseEntity<String>(s,HttpStatus.OK);
					 return rs;
				 }
				 else
				 {
					 throw new LedgerNotFoundException("Ledger Not Found...");
				 }
			 }
			 else
			 {
					throw new CustomerNotFoundException("Customer not found...");
				}
	   }
	   
	    @GetMapping("/getAllSanctionLettersAcceptedCustomers")
	    public ResponseEntity<List<Customer>> getAllSantionLaterAccepted()
	    {
	    	List <Customer> clist=si.getAllSantionLaterAccepted();
	    	if(clist.size()>0)
	    	{
	    		ResponseEntity<List<Customer>> rs = new ResponseEntity<List<Customer>>(clist,HttpStatus.OK);
	    		return rs;
	    	}
	    	else
	    	{
	    		throw new CustomerNotFoundException("Customers Not Found...");
	    	}
	    	
	    }
}
	   

