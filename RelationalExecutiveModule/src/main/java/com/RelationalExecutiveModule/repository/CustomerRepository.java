package com.RelationalExecutiveModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.RelationalExecutiveModule.model.Customer;
import com.RelationalExecutiveModule.model.Enquiry;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	@Query(name="getQuery1",value="from Enquiry inner join Cibil on enquiryStatus=?1 and cibilStatus=?2 and enquiryId=cibilId")
	List<Enquiry> getByEnquiryStatusAndCibilStatus(String string, String string2);

	@Query(name="getQuery2",value="from Customer c inner join c.enquiry e on e.enquiryStatus=?1")
	List<Customer> getCustomerByEnquiryStatus(String string);

}
