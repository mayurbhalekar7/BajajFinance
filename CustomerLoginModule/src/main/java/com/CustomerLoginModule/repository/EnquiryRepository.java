package com.CustomerLoginModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CustomerLoginModule.model.Enquiry;

@Repository
public interface EnquiryRepository  extends JpaRepository<Enquiry, Integer>{

}
