package com.CustomerLoginModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CustomerLoginModule.model.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {


	
}
