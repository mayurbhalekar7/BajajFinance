package com.CreditManagerModule.serviceimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import com.CreditManagerModule.model.Customer;
import com.CreditManagerModule.model.Enquiry;
import com.CreditManagerModule.model.SanctionLetter;
import com.CreditManagerModule.repository.CustomerRepository;
import com.CreditManagerModule.repository.EnquiryRepository;
import com.CreditManagerModule.repository.SanctionLetterRepository;
import com.CreditManagerModule.servicei.SanctionLetterServiceI;
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
public class SanctionLetterServiceImpl implements SanctionLetterServiceI{

	@Autowired
	SanctionLetterRepository sr;
	
	@Autowired
	CustomerRepository cr;
	
	@Autowired
	EnquiryRepository er;
	
	@Autowired
	JavaMailSender sender;

	@Autowired
    private ResourceLoader resourceLoader;
	
	@Override
	public Optional<Customer> getCustomerById(int customerId) {
		
		return cr.findById(customerId);
	}

	@Override
	public void updateSanctionLetterData(SanctionLetter sl) {
		sr.save(sl);
	}

	@Override
	public void calculateEMI(SanctionLetter sanctionLetter) {
		long sanctionAmount = sanctionLetter.getSanctionAmount();
		int tennure= sanctionLetter.getTennure();
		float interestRate = sanctionLetter.getInterestRate()/100;
		float totalEMI = (sanctionAmount+(sanctionAmount*interestRate))/tennure;
		sanctionLetter.setCalculatedEMI(totalEMI);
		sr.save(sanctionLetter);
	}

	@Override
	public List<Customer> getAllCustomersIsF2CMAndSanctionStatusNull()
	{
		return sr.getAllCustomersIsF2CMAndSanctionStatusNull("f2cm");
	}

	@Override
	public byte[] generateSanctionLetterPDF(SanctionLetter sanctionLetter) {
		
			String title = "Bajaj Finance";

			Document document = new Document(PageSize.A4);

			String content1 = "\n\n Dear " + sanctionLetter.getApplicantName()
					+ ","
					+ "\nBajaj Finance is Happy to informed you that your loan application has been approved. ";

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

			cell.setPhrase(new Phrase("Loan Amount Sanctioned", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf("₹" + sanctionLetter.getSanctionAmount()),
					font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Loan Tenure", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(sanctionLetter.getTennure()), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Interest Rate", font));
			table.addCell(cell);

			cell.setPhrase(
					new Phrase(String.valueOf(sanctionLetter.getInterestRate()) + " %", font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Sanction letter generated Date", font));
			table.addCell(cell);

			sanctionLetter.setSanctionDate(new Date());
			cell.setPhrase(
					new Phrase(String.valueOf(sanctionLetter.getSanctionDate()), font1));
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
			sanctionLetter.setSanctionLetterPDF(bytes);
	
			sr.save(sanctionLetter);
			
			return bytes;
		}

	@Override
	public void forwordToAccountHead(Enquiry enquiry) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(enquiry.getEmail());
		message.setSubject("Bajaj Finance Home Loan Enquiry Status");
		message.setText("Hello,"+enquiry.getFirstName()+" "+enquiry.getLastName()+",\nYour Home Loan Enquiry Status is forworded to Account Head."+
		"we will update you soon."+
		"\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
		sender.send(message);
		er.save(enquiry);
	}

	@Override
	public void forwardSanctionLetterToCustomer(Customer customer, Enquiry enquiry,SanctionLetter sanctionLetter) {
		
		MimeMessage mn =sender.createMimeMessage();
		try 
		{
			MimeMessageHelper helper = new MimeMessageHelper(mn,true);
			helper.setTo(enquiry.getEmail());
			helper.setSubject("Saction Letter of Home Loan");
			helper.setText("Hello,"+enquiry.getFirstName()+" "+enquiry.getLastName()+",\nYour Home Loan Sanction Letter generated successfull, Please find the attachment."
			+"\n\n\n Thanks & Regards,\n Bajaj Finance Team.");
			helper.addAttachment("Sanction Letter of "+sanctionLetter.getApplicantName()+" "+new Date()+".pdf" ,new ByteArrayResource( sanctionLetter.getSanctionLetterPDF()));
		    sender.send(mn);
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Optional<List<Enquiry>> getAllEnquriesByf2cm() {
		
		return er.findAllByEnquiryStatus("f2cm");
	}

	@Override
	public Optional<List<Customer>> getAllCustomersBySantionLetterStatus(String status) {
		return cr.getAllCustomersBySantionLetterStatus(status);
	}
}
