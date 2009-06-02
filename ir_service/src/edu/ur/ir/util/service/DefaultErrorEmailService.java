package edu.ur.ir.util.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import edu.ur.ir.ErrorEmailService;

/**
 * Default implementation of the Error Email Service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultErrorEmailService implements ErrorEmailService{

    /** Java mail sender to send emails */
    private JavaMailSender mailSender;
    
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultErrorEmailService.class);
	
	/** Subject line for the email */
	private String subject;
	
	/** email address to send the errors to*/
	private String toAddress;
	
	/**
	 * Send the error to the specified address.
	 * 
	 * @see edu.ur.ir.ErrorEmailService#sendError(java.lang.String, java.lang.String)
	 */
	public void sendError( String error) {
		 log.debug("send subscribers emails");
		 MimeMessage message = mailSender.createMimeMessage();
		 MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
		 try 
		 {
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setTo(toAddress);
				mimeMessageHelper.setText(error);
			    mailSender.send(message);
		 } 
		 catch (MessagingException e) 
		 {
			log.error("Messaging exception occured ", e);
		 }
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}


}
