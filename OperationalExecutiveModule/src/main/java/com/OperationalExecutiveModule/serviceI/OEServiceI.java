package com.OperationalExecutiveModule.serviceI;

import java.util.List;
import java.util.Optional;

import com.OperationalExecutiveModule.model.Customer;
import com.OperationalExecutiveModule.model.Enquiry;
import com.OperationalExecutiveModule.model.PersonalDocuments;

public interface OEServiceI {

	List<Enquiry> getAllEnquiries();

	Optional<Enquiry> checkCibilScore(int enquiryId);

	Optional<Enquiry> getByEmail(String email);

	void forwordToRelationalExecutive(Enquiry e);

	List<Enquiry> getEnquiry();

	List<Customer> getCustomer();

	Optional<Customer> getCustomerByID(int cId);

	void forDocumentVerification(Customer c);

	void forwordedToCM(Customer c);

	PersonalDocuments getPersonalDocuments(int customerId);
}

