package com.OperationalExecutiveModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.OperationalExecutiveModule.model.PersonalDocuments;
@Repository
public interface PersonalDocumentsRepository extends JpaRepository<PersonalDocuments, Integer>{

}
