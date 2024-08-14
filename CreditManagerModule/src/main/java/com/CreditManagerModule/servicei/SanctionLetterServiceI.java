package com.CreditManagerModule.servicei;

import java.util.List;
import java.util.Optional;

import com.CreditManagerModule.model.Customer;
import com.CreditManagerModule.model.Enquiry;
import com.CreditManagerModule.model.SanctionLetter;

public interface SanctionLetterServiceI {

	Optional<Customer> getCustomerById(int customerId);

	void updateSanctionLetterData(SanctionLetter sl);

	void calculateEMI(SanctionLetter sanctionLetter);

	List<SanctionLetter> getAllPendingSanctions();

	byte[] generateSanctionLetterPDF(SanctionLetter sanctionLetter);

	void forwordToAccountHead(Enquiry enquiry);

	void forwardSanctionLetterToCustomer(Customer customer, Enquiry enquiry,SanctionLetter sanctionLetter);

	Optional<List<Enquiry>> getAllEnquriesByf2cm();
}
