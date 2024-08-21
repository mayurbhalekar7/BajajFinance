package com.AccountHeadModule.serviceImpl;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import com.AccountHeadModule.exception.LedgerNotFoundException;
import com.AccountHeadModule.model.Customer;
import com.AccountHeadModule.model.Disbursment;
import com.AccountHeadModule.model.Ledger;
import com.AccountHeadModule.model.SanctionLetter;
import com.AccountHeadModule.repository.CoustomerRepository;
import com.AccountHeadModule.repository.DisbursmentRepository;
import com.AccountHeadModule.repository.LedgerRepository;
import com.AccountHeadModule.serviceI.ServiceI;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
	
	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public Optional<Customer> findById(int customerId) {
		Optional<Customer> op= cr.findById(customerId);
		return op;
	}

	@Override
	public void saveDisbursment(int customerId,Disbursment disbursment)
	{
		Customer cus=cr.findById(customerId).get();
		disbursment.setDisbursmentId(cus.getDisbursment().getDisbursmentId());
		disbursment.setDisbursmentStaus("pending");
		disbursment.setSanctionAmount(cus.getSanctionLetter().getSanctionAmount());
		disbursment.setTanuareInMonth(cus.getSanctionLetter().getTennure());
		disbursment.setTarnsferAmount(cus.getSanctionLetter().getSanctionAmount()-10000);
		dr.save(disbursment);
	}

	@Override
	public void sendDisbursment(Customer customer ,int disbursmentId) 
	{
		Disbursment disbursment=dr.findById(disbursmentId).get();
		disbursment.setDisbursmentStaus("done");
		dr.save(disbursment);
		       
		       
		   	String title = "Bajaj Finance";

			Document document = new Document(PageSize.A4);

			String content1 = "\n\n Dear " + customer.getEnquiry().getFirstName()+" "+customer.getEnquiry().getLastName()
					+ ","
					+ "\nBajaj Finance is Happy to informed you that your Disbursment is done into bank account. ";

			String content2 = "\n\nWe hope that you find the terms and conditions of this loan satisfactory "
					+ "and that it will help you meet your financial needs.\n\nIf you have any questions or need any assistance regarding your loan, "
					+ "please do not hesitate to contact us.\n\nWe wish you all the best and thank you for choosing us."
					+ "\n\nSincerely,\n\n" + "Mayur Bhalekar (Credit Manager)";

			ByteArrayOutputStream opt = new ByteArrayOutputStream();
			
			PdfWriter.getInstance(document, opt);
			document.open();

			Image img = null;
			try 
			{
				Resource resource =resourceLoader.getResource("classpath:/Images/Bajaj_finance_logo.png");
			//	img = Image.getInstance("/BajajFinanceProject/CreditManagerModule/src/main/resources/Images/Bajaj_finance_logo.png");
				img = Image.getInstance(resource.getURL());
				img.scalePercent(50, 50);
				img.setAlignment(Element.ALIGN_RIGHT);
				document.add(img);

			} catch (BadElementException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			Font titlefont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
			Paragraph titlepara = new Paragraph(title, titlefont);
			titlepara.setAlignment(Element.ALIGN_CENTER);
			document.add(titlepara);

			Font titlefont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent1 = new Paragraph(content1, titlefont2);
			document.add(paracontent1);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setWidths(new int[] { 2, 2 });
			table.setSpacingBefore(10);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(CMYKColor.WHITE);
			cell.setPadding(5);

			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setColor(5, 5, 161);

			Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(5, 5, 161);

			cell.setPhrase(new Phrase("Disbursed Amount", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf("₹" + disbursment.getTarnsferAmount()),
					font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Tennure In Month", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(disbursment.getTanuareInMonth()), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Interest Rate", font));
			table.addCell(cell);

			cell.setPhrase(
					new Phrase(String.valueOf(disbursment.getTanuareInMonth()) + " %", font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Disbursment letter generated Date", font));
			table.addCell(cell);

			//sanctionLetter.setSanctionDate(new Date());
			cell.setPhrase(
					new Phrase(String.valueOf(new Date()), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Total loan Amount with Interest", font));
			table.addCell(cell);

			document.add(table);

			Font titlefont3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent2 = new Paragraph(content2, titlefont3);
			document.add(paracontent2);
			document.close();
			
			ByteArrayInputStream byt = new ByteArrayInputStream(opt.toByteArray());
			byte[] bytes = byt.readAllBytes();
			
			MimeMessage mn =sender.createMimeMessage();
			try 
			{
				MimeMessageHelper helper = new MimeMessageHelper(mn,true);
				helper.setTo(customer.getEnquiry().getEmail());
				helper.setSubject("Disbursment Letter of Home Loan");
				helper.setText("Hello,"+customer.getEnquiry().getFirstName()+" "+customer.getEnquiry().getLastName()+",\nYour Home Loan Disbursement Letter generated successfull, Please find the attachment."
				+"\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
				helper.addAttachment("Disbursment Letter of "+customer.getSanctionLetter().getApplicantName()+" "+new Date()+".pdf" ,new ByteArrayResource(bytes));
			    sender.send(mn);
			}
			catch (MessagingException e)
			{
				e.printStackTrace();
			}
	}

	@Override
	public String createLedger(int customerId) {
		Customer cus=cr.findById(customerId).get();
		
		if(cus.getDisbursment().getDisbursmentStaus().equals("done"))
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
			return "Ledger created for: ";
		}
		else
		{
			return "Ledger not generated because disbursment status not done...";
		}
	}

	@Override
	public String updateLedger(int customerId,int ledgerId) {
		Optional<Ledger> ledger=lr.findById(ledgerId);
		if(!ledger.isPresent())
		{
			throw new LedgerNotFoundException("Ledger Not Found...");
		}
		
		if(ledger.get().getLoanStatus().equals("Defaulter"))
			
		{
			return "you are faild to pay consicative Three EMI's and Hence you have been Marked as Defaulter";
		}
		else 
		{   
			Customer customer=cr.findById(customerId).get();
		     
		    List<Ledger> ledgerList=customer.getLedeger();
		    ledger.get().setCurrentMonthEmiStatus("paid");
			double remainingAmount =ledger.get().getRemainingAmount()-ledger.get().getMonthlyEmi();
			double amountPaidTilldate=ledger.get().getAmountPaidTillDate()+ledger.get().getMonthlyEmi();
			for(Ledger le :ledgerList)
			{ 
				le.setAmountPaidTillDate(amountPaidTilldate);
				le.setRemainingAmount(remainingAmount);
			}
			customer.setLedeger(ledgerList);
			cr.save(customer);
			
			String title = "Bajaj Finance";

			Document document = new Document(PageSize.A4);

			String content1 = "\n\n Dear " + customer.getEnquiry().getFirstName()+" "+customer.getEnquiry().getLastName()
					+ ","
					+ "\nBajaj Finance is Happy to informed you that you paid your installment successfully";

			String content2 = "\n\nWe hope that you find the terms and conditions of this loan satisfactory "
					+ "and that it will help you meet your financial needs.\n\nIf you have any questions or need any assistance regarding your loan, "
					+ "please do not hesitate to contact us.\n\nWe wish you all the best and thank you for choosing us."
					+ "\n\nSincerely,\n\n" + "Mayur Bhalekar (Credit Manager)";

			ByteArrayOutputStream opt = new ByteArrayOutputStream();
			
			PdfWriter.getInstance(document, opt);
			document.open();

			Image img = null;
			try 
			{
				Resource resource =resourceLoader.getResource("classpath:/Images/Bajaj_finance_logo.png");
			//	img = Image.getInstance("/BajajFinanceProject/CreditManagerModule/src/main/resources/Images/Bajaj_finance_logo.png");
				img = Image.getInstance(resource.getURL());
				img.scalePercent(50, 50);
				img.setAlignment(Element.ALIGN_RIGHT);
				document.add(img);

			} catch (BadElementException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			Font titlefont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
			Paragraph titlepara = new Paragraph(title, titlefont);
			titlepara.setAlignment(Element.ALIGN_CENTER);
			document.add(titlepara);

			Font titlefont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent1 = new Paragraph(content1, titlefont2);
			document.add(paracontent1);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setWidths(new int[] { 2, 2 });
			table.setSpacingBefore(10);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(CMYKColor.WHITE);
			cell.setPadding(5);

			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setColor(5, 5, 161);

			Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(5, 5, 161);

			cell.setPhrase(new Phrase("Paid Installment", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf("₹" + ledger.get().getMonthlyEmi()),
					font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Remaining Amount", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(remainingAmount), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("EMI paid for Date:", font));
			table.addCell(cell);

			cell.setPhrase(
					new Phrase(String.valueOf(ledger.get().getNextEmiStartDate()), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("EMI Paid Date:", font));
			table.addCell(cell);

			//sanctionLetter.setSanctionDate(new Date());
			cell.setPhrase(
					new Phrase(String.valueOf(new Date()), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Total loan Amount with Interest", font));
			table.addCell(cell);

			document.add(table);

			Font titlefont3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent2 = new Paragraph(content2, titlefont3);
			document.add(paracontent2);
			document.close();
			
			ByteArrayInputStream byt = new ByteArrayInputStream(opt.toByteArray());
			byte[] bytes = byt.readAllBytes();
			
			MimeMessage mn =sender.createMimeMessage();
			try 
			{
				MimeMessageHelper helper = new MimeMessageHelper(mn,true);
				helper.setTo(customer.getEnquiry().getEmail());
				helper.setSubject("Reciept of EMI Pay");
				helper.setText("Hello,"+customer.getEnquiry().getFirstName()+" "+customer.getEnquiry().getLastName()+",\nYour Home Loan Installment Pay Reciept generated successfull, Please find the attachment."
				+"\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
				helper.addAttachment("EMI Pay Reciept of "+customer.getSanctionLetter().getApplicantName()+" "+new Date()+".pdf" ,new ByteArrayResource(bytes));
			    sender.send(mn);
			}
			catch (MessagingException e)
			{
				e.printStackTrace();
			}
			return "EMI for current Month Paid...";	
		}
	}

	@Override
	public String skipEmi(List<Ledger> l, int ledgerId,Customer customer) {
		Ledger le =lr.findById(ledgerId).get();
		le.setCurrentMonthEmiStatus("skip");
		lr.save(le);
		int count=0;
		for(Ledger led:l)
		{
			if(led.getCurrentMonthEmiStatus().equals("skip"))
			{ 
				count++;	
			}
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
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(customer.getEnquiry().getEmail());
		message.setSubject("Bajaj Finance EMI Installment Status");
		message.setText("Hello,"+customer.getEnquiry().getFirstName()+" "+customer.getEnquiry().getLastName()+",\nYour Home Loan EMI installment is skip for month: "+
		le.getNextEmiStartDate()+
		"\n\n\n Regards,\n Bajaj Finance Team.");
		sender.send(message);
		return "your Current Monthly Emi is Skip";
	}

	@Override
	public List<Customer> getAllSantionLaterAccepted() {
		
		List<Customer> li =cr.getAllSantionLaterAccepted("accept");
		return li;
	}

	@Override
	public boolean CheckCustomerAndLedgerMapping(int customerId, int ledgerId) {
		Customer customer = cr.findById(customerId).get();
		List<Ledger> ledgerList = customer.getLedeger();
		for(Ledger llist:ledgerList)
		{
			if(llist.getLedgerId()==ledgerId && llist.getCurrentMonthEmiStatus().equals("unpaid"))
			{
				return true;
			}
		}
		return false;
	}
}
