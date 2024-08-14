package com.RelationalExecutiveModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.RelationalExecutiveModule.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	@Query("from Customer c inner join c.enquiry e on e.enquiryStatus=?1")
	List<Customer> getAllPendingEnquiry(String string);



}
