package com.EnquiryModule.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EnquiryModule.model.Enquiry;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer> {

	Optional<List<Enquiry>> findAllByLoanStatus(String loanStatus);

	List<Enquiry> findAllByEnquiryStatus(String enquiryStatus);

	Optional<Enquiry> findByEmailAndEnquiryStatus(String email, String string);

	Optional<Enquiry> findByEmail(String email);

	Optional<Enquiry> findByEnquiryIdAndEnquiryStatus(int enquiryId, String string);
}
	