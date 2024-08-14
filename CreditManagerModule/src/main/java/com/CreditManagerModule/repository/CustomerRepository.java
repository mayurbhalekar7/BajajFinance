package com.CreditManagerModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CreditManagerModule.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
