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
public class HomeController {

	@Autowired
	RelationalExecuteServiceI si;

	@PostMapping(value = "/saveCustomer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> saveCustomer(@RequestPart("adharcard") MultipartFile adharcard,
			@RequestPart("pancard") MultipartFile pancard, @RequestPart("photo") MultipartFile photo,
			@RequestPart("signature") MultipartFile signature,
			@RequestPart("OccupationProof") MultipartFile OccupationProof,
			@RequestPart("ITreturn") MultipartFile ITreturn, @RequestPart("addressProof") MultipartFile addressProof,
			@RequestPart("data") String customer) 
	{
		ObjectMapper om = new ObjectMapper();

		try {

			Customer cusD = om.readValue(customer, Customer.class);

			int id = cusD.getEnquiry().getEnquiryId();

			Optional<Enquiry> op = si.findById(id);
			if (op.isPresent()) {
				Enquiry eq = op.get();
				if (eq.getEnquiryStatus().equals("f2re")) {
					PersonalDocuments doc = new PersonalDocuments();
					doc.setPhoto(photo.getBytes());
					doc.setAdharcard(adharcard.getBytes());
					doc.setPancard(pancard.getBytes());
					doc.setSignature(signature.getBytes());
					doc.setOccupationProof(OccupationProof.getBytes());
					doc.setITreturn(ITreturn.getBytes());
					doc.setAddressProof(addressProof.getBytes());
					cusD.setDocuments(doc);
					
					cusD.setEnquiry(eq);
					si.saveCustomer(cusD);
					
					ResponseEntity<String> rs = new ResponseEntity<String>("Send your Loan Application successfully...",
							HttpStatus.CREATED);
					return rs;
				}

				else {
					throw new EnquiryNotFoundException(" Enquiry Status  is not eligible for Registration");
				}

			} else {
				throw new EnquiryNotFoundException(" invalid enquiry id");
			}

		}
		catch (Exception e) {

			e.printStackTrace();
		}
		return null;

		
	}

	@GetMapping("/getCustomerById/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") int customerId) {

		Optional<Customer> op = si.getCustomerById(customerId);

		if (op.isPresent()) {
			return new ResponseEntity<Customer>(op.get(), HttpStatus.OK);
		}

		else {
			throw new CustomerNotFoundException("Invalid Customer Id");
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
			throw new CustomerNotFoundException("Invalid Customer Id");
		}

	}

	@GetMapping("/getAllCustomer")
	public ResponseEntity<List<Customer>> getAllCustomer() {
		List<Customer> listc = si.getAllCutomer();

		ResponseEntity<List<Customer>> list = new ResponseEntity<List<Customer>>(listc, HttpStatus.OK);

		return list;

	}

}
