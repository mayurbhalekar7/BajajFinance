package com.AdminModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.AdminModule.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	
	@Query(name="getQuery1",value="from Customer c inner join c.enquiry e on e.enquiryStatus=?1")
	List<Customer> getCustomersByEnquiryStatus(String eStatus);

	@Query(name="getQuery2",value="from Customer c inner join c.enquiry e inner join c.verification v on e.enquiryStatus=?1 and v.status=?2")
	List<Customer> getCustomerByEnquiryStatusAndVerifiStatus(String eStatus, String vStatus);

	@Query(name="getQuery3",value="from Customer c inner join c.enquiry e on e.loanStatus=?1")
	List<Customer> getCustomerByLoanStatus(String loanStatus);
}
