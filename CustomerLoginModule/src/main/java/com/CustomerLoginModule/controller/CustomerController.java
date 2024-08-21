package com.CustomerLoginModule.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<Customer> getCustomerByUsernameAndPassword(@PathVariable("username") String username,@PathVariable("password") String password)
	 {
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			return new ResponseEntity<Customer>(customer.get(), HttpStatus.OK);
		}
		else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}

/*	@GetMapping("/changePassword/{username}/{password}")
	public ResponseEntity<String> changePassword(@PathVariable("username") String username,@PathVariable("password") String password)
	{
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			String otp = si.changePassword();
			si.sendOTPToEmail(username, otp);
			return new ResponseEntity<String>("OTP Send to your email..." , HttpStatus.OK);
		}
		else
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}*/
	
	@GetMapping("/changePassword/{username}/{password}/{newPassword}")
	public ResponseEntity<String> changePassword(@PathVariable("username") String username,@PathVariable("password") String password,@PathVariable("newPassword") String newPassword)
	{
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			si.setnewPassword(customer, newPassword);
			return new ResponseEntity<String>("New Password set Successfully..." , HttpStatus.OK);
		}
		else
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}

	@GetMapping("/getBankDetails/{username}/{password}")
	public ResponseEntity<BankAccount> getBankDetails(@PathVariable("username") String username,@PathVariable("password") String password)
	{
		Optional<Customer> customer= si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			BankAccount bankAccount =customer.get().getAccount();
			return new ResponseEntity<BankAccount>(bankAccount, HttpStatus.OK);
		} else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}

	@GetMapping("/getAddress/{username}/{password}")
	public ResponseEntity<Address> getAddress(@PathVariable("username") String username,@PathVariable("password") String password)
	{
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			Address address = customer.get().getAddress();
			return new ResponseEntity<Address>(address, HttpStatus.OK);
		} 
		else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}

	@GetMapping("/getPersonalDocuments/{username}/{password}")
	public ResponseEntity<PersonalDocuments> getPersonalDocuments(@PathVariable("username") String username,@PathVariable("password") String password)
	{

		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			PersonalDocuments personalDocuments = customer.get().getDocuments();
			return new ResponseEntity<PersonalDocuments>(personalDocuments, HttpStatus.OK);
		} 
		else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}

	@GetMapping("/getEnquiry/{username}/{password}")
	public ResponseEntity<Enquiry> getEnquiry(@PathVariable("username") String username,@PathVariable("password") String password)
	{
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			Enquiry enquiry = customer.get().getEnquiry();
			return new ResponseEntity<Enquiry>(enquiry, HttpStatus.OK);
		} 
		else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}

	@GetMapping("/forgotPassword/{username}")
	public ResponseEntity<String> forgotPassword(@PathVariable("username") String username) 
	{
		Optional<Customer> customer = si.findUsername(username);
		if (customer.isPresent()) 
		{
			si.sendPasswordOnEmail(customer.get());
			return new ResponseEntity<String>("password is Send to Customer vai Email", HttpStatus.OK);
		} 
		else 
		{
			throw new CustomerNotFoundException("Invalid username");
		}
	}

	@PutMapping(value = "updateCustomer/{username}/{password}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateCustomer(@PathVariable("username") String username,@PathVariable("password") String password,
			@RequestPart("adharcard") MultipartFile adharcard, @RequestPart("pancard") MultipartFile pancard,
			@RequestPart("photo") MultipartFile photo, @RequestPart("signature") MultipartFile signature,
			@RequestPart("OccupationProof") MultipartFile OccupationProof,
			@RequestPart("ITreturn") MultipartFile ITreturn, @RequestPart("addressProof") MultipartFile addressProof,
			@RequestPart("customerData") String customerData) {
		ObjectMapper om = new ObjectMapper();
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			try 
			{
				Customer cusD = om.readValue(customerData, Customer.class);
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
				ResponseEntity<String> rs = new ResponseEntity<String>("Customer information updated  successfully...",HttpStatus.CREATED);
				return rs;

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				ResponseEntity<String> rs = new ResponseEntity<String>("Customer information not updated...",HttpStatus.BAD_REQUEST);
				return rs;
			}	
		} 
		else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}
	
	@GetMapping("/getCustomerSactionLetterPDF/{username}/{password}")
	public ResponseEntity<byte[]> getCustomerSactionLetterPDF(@PathVariable("username") String username,@PathVariable("password") String password)
	{

		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			byte[] sanctionLtterPDF = customer.get().getSanctionLetter().getSanctionLetterPDF();
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDispositionFormData("Attachment","Sanction Letter of "+customer.get().getSanctionLetter().getApplicantName()+" "+new Date()+".pdf");
			return new ResponseEntity<byte[]>(sanctionLtterPDF,headers,HttpStatus.OK);
		} 
		else 
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}
	
	@GetMapping("/setSactionLetterStatus/{username}/{password}/{status}")
	public ResponseEntity<String> setSactionLetterStatus(@PathVariable("username") String username,@PathVariable("password") String password,@PathVariable("status") String status)
	{
		Optional<Customer> customer = si.getCustomerByUsernameAndPassword(username, password);
		if (customer.isPresent()) 
		{
			customer.get().getSanctionLetter().setSanctionLetterStatus(status);
			si.updateCustomer(customer.get());
			return new ResponseEntity<String>("Sanction Letter Status Updated Successfully...", HttpStatus.OK);
			
		}
		else
		{
			throw new CustomerNotFoundException("Username and Password is incorrect...");
		}
	}
}
