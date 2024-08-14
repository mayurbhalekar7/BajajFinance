package com.CustomerLoginModule.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;

import com.CustomerLoginModule.exception.CustomerNotFoundException;
import com.CustomerLoginModule.model.Address;
import com.CustomerLoginModule.model.BankAccount;
import com.CustomerLoginModule.model.Customer;
import com.CustomerLoginModule.model.Enquiry;
import com.CustomerLoginModule.model.PersonalDocuments;
import com.CustomerLoginModule.servicei.ServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CustomerController {

	@Autowired
	ServiceI si;

	@GetMapping("/getCustomerByUsernameAndPassword/{username}/{password}")
	public ResponseEntity<Customer> getCustomerByUsernameAndPassword(@PathVariable("username") String username,
			                                                    @PathVariable("password") String password)
	 {
		Optional<Customer> op = si.getCustomerByUsernameAndPassword(username, password);

		if (op.isPresent()) {
			return new ResponseEntity<Customer>(op.get(), HttpStatus.OK);
		}

		else {
			throw new CustomerNotFoundException("wrong username and password");
		}
	}

	/*
	 * @PutMapping("/updateCustomerByEmailId/{username}") public
	 * ResponseEntity<String> updateCustomerById(@PathVariable("username")String
	 * EmailId,@RequestBody Customer cus) { Optional<Customer> cust=
	 * Optional.of(si.findByUsername(EmailId));
	 * 
	 * if(cust.isPresent()) { si.updateCustomerByEmail(cus); return new
	 * ResponseEntity<String>("Customer updated",HttpStatus.OK); } else { throw new
	 * CustomerNotFoundException("invalid customer id"); } }
	 */
	@GetMapping("/generate-otp/{username}")
	public String generateAndSendOtp(@PathVariable("username") String username) 
	{
		String otp = si.generateOtp();
		si.sendOtpEmail(username, otp);
		return "OTP sent to your email: " + username;
	}

	@GetMapping("/getCustomerById/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") int CustomerId) 
	{
		Optional<Customer> op = si.getCustomerById(CustomerId);
		if (op.isPresent()) {
			return new ResponseEntity<Customer>(op.get(), HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException("invalid Customer Id");
		}
	}

	@GetMapping("/getBankDetails/{customerId}")
	public ResponseEntity<BankAccount> getBankDetails(@PathVariable("customerId") int CustomerId)
	{
		Optional<Customer> op = si.getCustomerById(CustomerId);
		if (op.isPresent()) {
			BankAccount bk = si.getBankAccount(CustomerId);
			return new ResponseEntity<BankAccount>(bk, HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException("invalid Bank Details");
		}
	}

	@GetMapping("/getAddress/{CustomerId}")
	public ResponseEntity<Address> getAddress(@PathVariable("CustomerId") int CustomerId) {
		Optional<Customer> oc = si.getCustomerById(CustomerId);
		if (oc.isPresent()) {
			Address bk = si.getAddress(CustomerId);
			return new ResponseEntity<Address>(bk, HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException("invalid Address Details");
		}
	}

	@GetMapping("/getPersonalDocuments/{CustomerId}")
	public ResponseEntity<PersonalDocuments> getPersonalDocuments(
			@PathVariable("CustomerId") int CustomerId) {

		Optional<Customer> oc = si.getCustomerById(CustomerId);

		if (oc.isPresent()) {
			PersonalDocuments bk = si.getPersonalDocuments(CustomerId);

			return new ResponseEntity<PersonalDocuments>(bk, HttpStatus.OK);
		} else {

			throw new CustomerNotFoundException("invalid PersonalDocuments Details");
		}
	}

	@GetMapping("/getEnquiry/{CustomerId}")
	public ResponseEntity<Enquiry> getEnquiry(@PathVariable("CustomerId") int CustomerId) {

		Optional<Customer> sp = si.getCustomerById(CustomerId);

		if (sp.isPresent()) {
			Enquiry bk = si.getEnquiry(CustomerId);

			return new ResponseEntity<Enquiry>(bk, HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException("invalid Enquiry Details");
		}

	}

	@GetMapping("/forgotepasswordByCustomer/{emailId}")
	public ResponseEntity<String> forgotPassword(@PathVariable("emailId") String emailId) {

		Optional<Customer> op = si.findUsername(emailId);

		if (op.isPresent()) {
			Customer cus = op.get();
			si.sendOPtPasswod(cus);
			return new ResponseEntity<String>("password otp is Send to Customer vai Email", HttpStatus.OK);
		} else {
			throw new CustomerNotFoundException("invalid email");
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
		if (op.isPresent()) {

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
			throw new CustomerNotFoundException("Invalid Customer Id");
		}

	}
}
