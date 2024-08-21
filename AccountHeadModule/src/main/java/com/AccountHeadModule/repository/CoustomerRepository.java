package com.AccountHeadModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.AccountHeadModule.model.Customer;

public interface CoustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("from Customer c inner join c.sanctionLetter s on s.sanctionLetterStatus=?1")
	List<Customer> getAllSantionLaterAccepted(String string);
}
