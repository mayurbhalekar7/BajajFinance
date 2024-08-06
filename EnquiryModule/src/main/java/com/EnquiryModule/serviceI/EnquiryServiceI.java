package com.EnquiryModule.serviceI;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.EnquiryModule.model.Enquiry;

public interface EnquiryServiceI {

	void addEnquiry(Enquiry eq);

	 List<Enquiry> getAllEnquiries();

	Optional<Enquiry> getEnquriryById(int enquiryId);

	void updateEnquiry(int eid, Enquiry eq);


	void deleteEnquiry(int eid);

	Optional<List<Enquiry>> getEnquiryByLoanStatus(String loanStatus);

	void updateEnquiryStatus(int enquiryId, Enquiry eq);

	


}
