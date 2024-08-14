package com.CustomerLoginModule.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CustomerLoginModule.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

	Optional<Customer> getByUsernameAndPassword(String username, String password);

	 public Optional<Customer> findByUsername(String emailId);

}
