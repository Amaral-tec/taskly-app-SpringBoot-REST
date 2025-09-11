package com.amaral.taskly.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ServiceSendEmail {

    private String userName = "amaraljje@gmail.com";
	private String password = "Educ131919$";

    @Async
	public void sendEmailHtml(String subject, String body, String recipient) throws UnsupportedEncodingException {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "false");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		
	}
}
