package com.AccountHeadModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AccountHeadModule.model.Disbursment;

@Repository
public interface DisbursmentRepository extends JpaRepository<Disbursment,Integer> {

}
