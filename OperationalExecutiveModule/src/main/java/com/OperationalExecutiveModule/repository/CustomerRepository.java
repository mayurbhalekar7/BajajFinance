package com.OperationalExecutiveModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OperationalExecutiveModule.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	@Query(name="getQuery",value="from Customer c inner join c.enquiry e inner join c.verification v on e.enquiryStatus=?1 and v.status=?2")
	List<Customer> getByVerifiStatusPendingCustomer(String status1,String status2);

  

}
