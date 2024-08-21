package com.RelationalExecutiveModule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.RelationalExecutiveModule.exception.CustomerNotFoundException;
import com.RelationalExecutiveModule.exception.EnquiryNotFoundException;
import com.RelationalExecutiveModule.model.Customer;
import com.RelationalExecutiveModule.model.Enquiry;
import com.RelationalExecutiveModule.model.PersonalDocuments;
import com.RelationalExecutiveModule.service.RelationalExecuteServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
public class RelationalExecutiveController {

	@Autowired
	RelationalExecuteServiceI si;

	@PostMapping(value = "/saveCustomer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> saveCustomer(@RequestPart("adharcard") MultipartFile adharcard,
	@RequestPart("pancard") MultipartFile pancard, @RequestPart("photo") MultipartFile photo,
	@RequestPart("signature") MultipartFile signature,
	@RequestPart("OccupationProof") MultipartFile OccupationProof,
	@RequestPart("ITreturn") MultipartFile ITreturn, @RequestPart("addressProof") MultipartFile addressProof,
	@RequestPart("customerData") String customerData) 
	{
		ObjectMapper om = new ObjectMapper();
		try 
		{
			Customer customer = om.readValue(customerData, Customer.class);
			int id = customer.getEnquiry().getEnquiryId();
			Optional<Enquiry> enquiry = si.findById(id);
			if (enquiry.isPresent()) 
			{
				Enquiry eq = enquiry.get();
				if (eq.getEnquiryStatus().equals("f2re"))
				{
					PersonalDocuments doc = new PersonalDocuments();
					doc.setPhoto(photo.getBytes());
					doc.setAdharcard(adharcard.getBytes());
					doc.setPancard(pancard.getBytes());
					doc.setSignature(signature.getBytes());
					doc.setOccupationProof(OccupationProof.getBytes());
					doc.setITreturn(ITreturn.getBytes());
					doc.setAddressProof(addressProof.getBytes());
					customer.setDocuments(doc);
					customer.setEnquiry(eq);
					si.saveCustomer(customer);
					ResponseEntity<String> rs = new ResponseEntity<String>("Customer Data Saved Successfully...",HttpStatus.CREATED);
					return rs;
				}
				else 
				{
					throw new EnquiryNotFoundException("Enquiry is not under Relational Executive");
				}
			} 
			else 
			{
				throw new EnquiryNotFoundException("Enquiry Not Found");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		ResponseEntity<String> rs = new ResponseEntity<String>("Customer Data Not Saved...",HttpStatus.BAD_REQUEST);
		return rs;
	}

	@GetMapping("/getAnyCustomerById/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") int customerId) {

		Optional<Customer> op = si.getCustomerById(customerId);
		if (op.isPresent()) {
			return new ResponseEntity<Customer>(op.get(), HttpStatus.OK);
		}
		else 
		{
			throw new CustomerNotFoundException("Customer Not Found...");
		}
	}

	@PutMapping(value = "updateCustomer/{customerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateCustomer(@PathVariable("customerId") int customerId,
			@RequestPart("adharcard") MultipartFile adharcard, @RequestPart("pancard") MultipartFile pancard,
			@RequestPart("photo") MultipartFile photo, @RequestPart("signature") MultipartFile signature,
			@RequestPart("OccupationProof") MultipartFile OccupationProof,
			@RequestPart("ITreturn") MultipartFile ITreturn, @RequestPart("addressProof") MultipartFile addressProof,
			@RequestPart("data") String customer) {
		ObjectMapper om = new ObjectMapper();
		Optional<Customer> op = si.getCustomerById(customerId);
		if (op.isPresent())
		  {
			
			try {

				Customer cusD = om.readValue(customer, Customer.class);
				PersonalDocuments doc = new PersonalDocuments();
				doc.setPhoto(photo.getBytes());
				doc.setAdharcard(adharcard.getBytes());
				doc.setPancard(pancard.getBytes());
				doc.setSignature(signature.getBytes());
				doc.setOccupationProof(OccupationProof.getBytes());
				doc.setITreturn(ITreturn.getBytes());
				doc.setAddressProof(addressProof.getBytes());
				cusD.setDocuments(doc);

				si.updateCustomer(cusD);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ResponseEntity<String> rs = new ResponseEntity<String>("Customer information updated  successfully...",
					HttpStatus.CREATED);
			return rs;
		} else {
			throw new CustomerNotFoundException("Customer Not Found...");
		}

	}

	@GetMapping("/getAllF2RECustomers")
	public ResponseEntity<List<Customer>> getAllF2RECustomers() {
		List<Customer> clist = si.getAllF2RECustomers();
        if(clist.size()>0)
        {
        	ResponseEntity<List<Customer>> re = new ResponseEntity<List<Customer>>(clist, HttpStatus.OK);
        	return re;
        }
        else
        {
        	throw new CustomerNotFoundException("Customer Not Found...");
        }
	}
	
	@GetMapping("/customerForwordToOEforDocumentVerification/{customerId}")
	public ResponseEntity<String> forwordToOE(@PathVariable int customerId) 
	{
		Optional<Customer> customer = si.getCustomerById(customerId);
		if(customer.isPresent()&& customer.get().getEnquiry().getEnquiryStatus().equals("Registered")) 
		{
			si.forwordToOE(customerId);
			return new ResponseEntity<String>("Customer is Forword to Operational Executive...", HttpStatus.OK);
		}
		else 
		{
			throw new CustomerNotFoundException("Customer Not Found...");
		}
	}
	
	@GetMapping("/getByF2reAndGoodEnquiry")
	public ResponseEntity<List<Enquiry>> getByF2reAndGoodEnquiry() {
		List<Enquiry> elist = si.getByF2reAndGoodEnquiry();
		if(elist.size()>0)
		{
			ResponseEntity<List<Enquiry>> re=new ResponseEntity<List<Enquiry>>(elist,HttpStatus.OK);
			return re;
		}
		else
		{
			throw new EnquiryNotFoundException("Enquiry Not Found...");
		}
	}
	
	@GetMapping("/getAllRegisteredCustomers")
	public ResponseEntity<List<Customer>> getAllRegisteredCustomers() {
		List<Customer> clist = si.getAllRegisteredCustomers();
        if(clist.size()>0)
        {
        	ResponseEntity<List<Customer>> re = new ResponseEntity<List<Customer>>(clist, HttpStatus.OK);
        	return re;
        }
        else
        {
        	throw new CustomerNotFoundException("Customer Not Found...");
        }
	}
	
}
