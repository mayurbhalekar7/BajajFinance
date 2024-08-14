package com.AccountHeadModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AccountHeadModule.model.Ledger;

@Repository
public interface LedgerRepository  extends JpaRepository<Ledger, Integer>{

}
