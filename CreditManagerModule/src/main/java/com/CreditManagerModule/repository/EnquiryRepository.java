package com.CreditManagerModule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CreditManagerModule.model.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Integer>{

	Optional<List<Enquiry>> findAllByEnquiryStatus(String string);

}
