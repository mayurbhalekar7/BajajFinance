package com.AccountHeadModule.serviceImpl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.AccountHeadModule.exception.CustomerNotFoundException;
import com.AccountHeadModule.model.Customer;
import com.AccountHeadModule.model.Disbursment;
import com.AccountHeadModule.model.Ledger;
import com.AccountHeadModule.model.SanctionLetter;
import com.AccountHeadModule.repository.CoustomerRepository;
import com.AccountHeadModule.repository.DisbursmentRepository;
import com.AccountHeadModule.repository.LedgerRepository;
import com.AccountHeadModule.serviceI.ServiceI;

@Service
public class ServiceImpl implements ServiceI {

	@Autowired
	DisbursmentRepository dr;
	
	@Autowired
	CoustomerRepository cr;
	
	@Autowired
	LedgerRepository lr;
	
	@Autowired
	 private JavaMailSender sender;

	@Override
	public Optional<Customer> findById(int customerId) {
		Optional<Customer> op= cr.findById(customerId);
		return op;
	}

	@Override
	public void saveDisbursment(int customerId,Disbursment disbursment)
	{
		Customer cus=cr.findById(customerId).get();
		disbursment.setDisbursmentStaus("pending");
		disbursment.setSanctionAmount(cus.getSanctionLetter().getSanctionAmount());
		disbursment.setTanuareInMonth(cus.getSanctionLetter().getTennure());
		disbursment.setTarnsferAmount(cus.getSanctionLetter().getSanctionAmount()-10000);
	
		dr.save(disbursment);
		
	}

	@Override
	public void sendDisbursment(int disbursmentId) {
		Disbursment dis=dr.findById(disbursmentId).get();
		       dis.setDisbursmentStaus("done");
		       dr.save(dis);
		       
		
	}

	@Override
	public void createLedger(int customerId) {
		Customer cus=cr.findById(customerId).get();
		
		if(cus.getSanctionLetter().getSanctionLetterStatus().equals("accept"))
		
		{
			Calendar current_date_cal = Calendar.getInstance();
			Date current_date = new Date();
			current_date_cal.setTime(current_date);
			
			Calendar start_date_calendar = Calendar.getInstance();
			start_date_calendar.set(2024, Calendar.AUGUST,15);
			Date emi_start_date = start_date_calendar.getTime();
			
			Calendar end_date_calendar =Calendar.getInstance();
			end_date_calendar.set(2024,Calendar.AUGUST,20);
			Date emi_end_date= end_date_calendar.getTime(); 
			
			current_date_cal.add(current_date_cal.MONTH, 12);
			Date loan_end_date= current_date_cal.getTime();
			
			List<Ledger> ledgerList = new ArrayList<>();
			for(int i=1; i<=cus.getSanctionLetter().getTennure(); i++)
			{
				SanctionLetter sanction = cus.getSanctionLetter();
				Ledger l = new Ledger();
				l.setLedgerCreatedDate(current_date);
				l.setTotalPrincipalAmount(sanction.getSanctionAmount());
				l.setPayableAmount(sanction.getSanctionAmount()+(sanction.getSanctionAmount()*sanction.getInterestRate()/100));
				l.setTenure(sanction.getTennure());
				l.setMonthlyEmi(sanction.getCalculatedEMI());
				l.setAmountPaidTillDate(0);
				l.setRemainingAmount(sanction.getSanctionAmount()+(sanction.getSanctionAmount()*sanction.getInterestRate()/100));
				l.setNextEmiStartDate(emi_start_date);
				start_date_calendar.add(Calendar.MONTH, 1);
				emi_start_date =start_date_calendar.getTime();
				l.setNextEmiEndDate(emi_end_date);
				end_date_calendar.add(Calendar.MONTH, 1);
				emi_start_date =start_date_calendar.getTime();
				l.setPreviousEmiStatus("Unpaid");
				l.setCurrentMonthEmiStatus("unpaid");
				l.setNextEmiEndDate(loan_end_date);
				l.setLoanStatus("ongoing");
				ledgerList.add(l);
				cus.setLedeger(ledgerList);
				cr.save(cus);
			}
		}
	}

	@Override
	public String updateLedger( int customerId, int ledgerId) {
		Ledger led=lr.findById(ledgerId).get();
		
		if(led.getLoanStatus().equals("Defaulter"))
			
		{
			return "you are faild to pay consicative Three EMI's and Hence you have been Marked as Defaulter";
		}
		else 
		{   Customer cus=cr.findById(customerId).get();
		     
		    List<Ledger> l=cus.getLedeger();
		    led.setCurrentMonthEmiStatus("paid");
			double remainingAmount =led.getRemainingAmount()-led.getMonthlyEmi();
			double amountPaidTilldate=led.getAmountPaidTillDate()+led.getMonthlyEmi();
			for(Ledger le :l)
			{ 
			le.setAmountPaidTillDate(amountPaidTilldate);
			le.setRemainingAmount(remainingAmount);
			}
			cus.setLedeger(l);
			
			cr.save(cus);
			
			return "EMI for current Month Paid";
			
		}
		
	}

	@Override
	public String skipEmi(List<Ledger> l, int ledgerId) {
		Ledger le =lr.findById(ledgerId).get();
		le.setCurrentMonthEmiStatus("skip");
		lr.save(le);
		int count=0;
		for(Ledger led:l)
		{
			if(led.getCurrentMonthEmiStatus().equals("skip"))
			{ count++;}
			if(led.getCurrentMonthEmiStatus().equals("paid"))
			{
				count=0;
			}
			if(led.getCurrentMonthEmiStatus().equals("unpaid"))
			{
				if(count==3)
					
				{
					led.setLoanStatus("Defaulter");
					lr.save(led);
				}	
			}
		}
		
		return "your Current Monthly Emi is Skip";
	}

	@Override
	public List<Customer> getAllSantionLaterAccepted() {
		
		List<Customer> li =cr.getAllSantionLaterAccepted("accept");
		return li;
	}
	
	

            
            	
            	
            	
            	
			
			
			
			
				
			
		
}
