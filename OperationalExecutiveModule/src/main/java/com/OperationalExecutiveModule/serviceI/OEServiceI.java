package com.OperationalExecutiveModule.serviceI;

import java.util.List;
import java.util.Optional;

import com.OperationalExecutiveModule.model.Enquiry;

public interface OEServiceI {

	List<Enquiry> getAllEnquiries();

	Optional<Enquiry> checkCibilScore(int enquiryId);

	Optional<Enquiry> getByEmail(String email);

	void forwordToRelationalExecutive(Enquiry e);

	List<Enquiry> getEnquiry();
}
