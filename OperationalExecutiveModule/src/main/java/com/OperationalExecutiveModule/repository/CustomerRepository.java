package com.OperationalExecutiveModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OperationalExecutiveModule.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

	@Query(name="getQuery",value="from Customer inner join Verification on status=?1")
	List<Customer> getByVerifiStatusPendingCustomer(String status);



}
