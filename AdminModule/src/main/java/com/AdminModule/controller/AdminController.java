package com.AdminModule.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.AdminModule.model.Cibil;
import com.AdminModule.model.Customer;
import com.AdminModule.model.Enquiry;
import com.AdminModule.model.User;
import com.AdminModule.serviceI.ServiceI;

@RestController
public class AdminController {

	@Autowired
	ServiceI si;
	
   @PostMapping("/addUsers")
   public ResponseEntity<String> addUser(@RequestBody User u)
   {
	   si.addUsers(u); 
	   ResponseEntity<String> re=new ResponseEntity<String>("User added...",HttpStatus.ACCEPTED);
	   return re;
   }
   
   @GetMapping("/getAllEnquiries/{choice}")
   public List<Enquiry> getAllEnquiries(@PathVariable("choice") int choice)
   {
	   List<Enquiry> elist=si.getAllEnquiries();
	   if(choice==1)
	   {
		   List<Enquiry> rlist=elist.stream().sorted(Comparator.comparing(Enquiry::getFirstName)).collect(Collectors.toList());
		   return rlist;
	   }
	   else if(choice==2)
	   {
		   List<Enquiry> rlist=elist.stream().sorted(Comparator.comparing(Enquiry::getLastName)).collect(Collectors.toList());
		   return rlist;
	   }
	   else if(choice==3)
	   {
		   List<Enquiry> rlist=elist.stream().sorted(Comparator.comparing(Enquiry::getAge)).collect(Collectors.toList());
		   return rlist;
	   }
	   else
		   return elist;
   }
   
   @GetMapping("/getEnquiriesByEnquiryStatus/{enquiryStatus}")
   public List<Enquiry> getEnquiriesByEnquiryStatus(@PathVariable("enquiryStatus") String eStatus)
   {
	   List<Enquiry> elist=si.getEnquiriesByEnquiryStatus(eStatus);
	   return elist;
   }
   
   @GetMapping("/getAllCustomers/{choice}")
   public List<Customer> getAllCustomers(@PathVariable("choice") int choice)
   {
	  List<Customer> clist=si.getAllCustomers();
	  
	  if(choice==1)
	  {
	  List<Customer> rlist=clist.stream().sorted(Comparator.comparing(Customer::getEnquiry,Comparator.comparing(Enquiry::getCibil,Comparator.comparing(Cibil::getCibilScore)))).collect(Collectors.toList());
	   return rlist;
	  }
	  else if(choice==2)
	  {
	  List<Customer> rlist=clist.stream().sorted(Comparator.comparing(Customer::getEnquiry,Comparator.comparing(Enquiry::getFirstName))).collect(Collectors.toList());
	  return rlist;
	  }
	  else if(choice==3)
	  {
	  List<Customer> rlist=clist.stream().sorted(Comparator.comparing(Customer::getEnquiry,Comparator.comparing(Enquiry::getLastName))).collect(Collectors.toList());
	  return rlist;
	  }
	  else if(choice==4)
	  {
	  List<Customer> rlist=clist.stream().sorted(Comparator.comparing(Customer::getEnquiry,Comparator.comparing(Enquiry::getAge))).collect(Collectors.toList());
	  return rlist;
	  }
	  return clist;
   }
   
   @GetMapping("/getCustomersByEnquiryStatus/{enquiryStatus}")
   public List<Customer> getCustomersByEnquiryStatus(@PathVariable("enquiryStatus") String eStatus)
   {
	   List<Customer> clist=si.getCustomersByEnquiryStatus(eStatus); 
	   return clist;
   }
   
   @GetMapping("getCustomerByEnquiryStatusAndDocumVerifiStatus/{enquiryStatus}/{status}")
   public List<Customer> getCustomerByEnquiryStatusAndVerifiStatus(@PathVariable("enquiryStatus") String eStatus,@PathVariable("status") String vStatus)
   {
	   List<Customer> clist=si.getCustomerByEnquiryStatusAndVerifiStatus(eStatus,vStatus); 
	   return clist;   
   }
   
   
   @GetMapping("/getAllUsers")
   public List<User> getAllUsers()
   {
	   List<User> ulist=si.getAllUsers();
	   return ulist;
   }
   
  
   @GetMapping("/getUsersByAccoType/{accountType}")
   public List<User> getUsersByAccoType(@PathVariable("accountType") String accountType)
   {
	   List<User> ulist=si.getUsersByAccoType(accountType);
	   return ulist;
   }
   
   @GetMapping("/getCustomersByLoanStatus/{loanStatus}")
   public List<Customer> getCustomerByLoanStatus(@PathVariable("loanStatus") String loanStatus)
   {
	   List<Customer> clist=si.getCustomerByLoanStatus(loanStatus); 
	   return clist; 
   }
   
//   public ResponseEntity<String> setIntrestRate()
//   {
//	  ResponseEntity<String>  
//   }
}
