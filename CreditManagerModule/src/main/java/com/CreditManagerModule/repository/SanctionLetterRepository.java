package com.CreditManagerModule.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.CreditManagerModule.model.Customer;
import com.CreditManagerModule.model.SanctionLetter;
@Repository
public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, Integer>{

	@Query("from Customer c inner join c.sanctionLetter s inner join c.enquiry e on s.sanctionLetterStatus IS NULL and e.enquiryStatus=?1")
	List<Customer> getAllCustomersIsF2CMAndSanctionStatusNull(String string);
}
