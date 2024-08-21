package com.CreditManagerModule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.CreditManagerModule.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	@Query("from Customer c inner join c.sanctionLetter s on s.sanctionLetterStatus=?1")
	Optional<List<Customer>> getAllCustomersBySantionLetterStatus(String status);

}
