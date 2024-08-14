package com.AdminModule.serviceI;

import java.util.List;

import com.AdminModule.model.Customer;
import com.AdminModule.model.Enquiry;
import com.AdminModule.model.User;

public interface ServiceI {

	void addUsers(User u);

	List<Enquiry> getAllEnquiries();

	List<Enquiry> getEnquiriesByEnquiryStatus(String eStatus);

	List<Customer> getAllCustomers();

	List<Customer> getCustomersByEnquiryStatus(String eStatus);

	List<Customer> getCustomerByEnquiryStatusAndVerifiStatus(String eStatus, String vStatus);

	List<User> getAllUsers();

	List<User> getUsersByAccoType(String accountType);

	List<Customer> getCustomerByLoanStatus(String loanStatus);

}
