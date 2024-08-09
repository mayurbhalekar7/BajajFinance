package com.RelationalExecutiveModule.service;

import java.util.List;
import java.util.Optional;
import com.RelationalExecutiveModule.model.Customer;
import com.RelationalExecutiveModule.model.Enquiry;

public interface RelationalExecuteServiceI {

	public void saveCustomer(Customer cusD);

	public Optional<Customer> getCustomerById(int customerId);

	public void updateCustomer(Customer cusD);

	public List<Customer> getAllCutomer();

	public Optional<Enquiry> findById(int id);

}
