package com.CustomerLoginModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CustomerLoginModule.model.PersonalDocuments;

public interface PersonalDocumentsRepository extends JpaRepository<PersonalDocuments,Integer>{

}
