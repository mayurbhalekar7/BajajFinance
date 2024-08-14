package com.CreditManagerModule.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CreditManagerModule.model.SanctionLetter;
@Repository
public interface SanctionLetterRepository extends JpaRepository<SanctionLetter, Integer>{

	List<SanctionLetter> findAllBySanctionLetterStatus(String string);

	

}
