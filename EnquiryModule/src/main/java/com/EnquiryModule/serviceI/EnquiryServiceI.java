package com.EnquiryModule.serviceI;

import java.util.List;
import java.util.Optional;
import com.EnquiryModule.model.Enquiry;

public interface EnquiryServiceI {

	void addEnquiry(Enquiry eq);

	List<Enquiry> getAllEnquiries(String enquiryStatus);

	Optional<Enquiry> getEnquriryById(int enquiryId);

	void updateEnquiry(Enquiry eq);

	void deleteEnquiry(int eid);

	Optional<List<Enquiry>> getEnquiryByLoanStatus(String loanStatus);

	void enquiryForwardToOE(Enquiry enquiry);

	Optional<Enquiry> getEnquiryByEmail(String email);

	


}
