package com.AdminModule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AdminModule.model.User;

@Repository
public interface AdminRepository extends JpaRepository<User, Integer>{

	List<User> getByAccountType(String accountType);

}
