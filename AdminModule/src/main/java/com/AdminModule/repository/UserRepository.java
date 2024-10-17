package com.AdminModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AdminModule.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User getByUsernameAndPassword(String username,String Password);
}
