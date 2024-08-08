package com.OperationalExecutiveModule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OperationalExecutiveModule.model.Enquiry;

@Repository
public interface OERepository extends JpaRepository<Enquiry, Integer> {

	Optional<Enquiry> getByEmail(String email);
	
	List<Enquiry> getByEnquiryStatus(String enquiryStatus);

	Optional<Enquiry> getByEnquiryIdAndEnquiryStatus(int enquiryId, String string);

	@Query(name="getQuery",value="from Enquiry inner join Cibil on enquiryStatus=?1 and cibilStatus=?2 and enquiryId=cibilId")
	List<Enquiry> getByEnquiryStatusAndCibilStatus(String enquiryStatus, String cibilStatus);
	
}
