package com.mailer;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer {

	public static void send(final String from, final String password, final String[] to, final String sub,
			final String msg, final String pdf, final String pdfPath) throws MessagingException {
		// Get properties object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		// get Session
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
		// compose message

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));		
		Address[] addresses = new Address[to.length];
		int i = 0;
		for (String t : to) {
			addresses[i++] = new InternetAddress(t);
		}
		message.addRecipients(Message.RecipientType.TO, addresses);
		message.setSubject(sub);

		// Create MimeBodyPart object and set your message text
		BodyPart messageBodyPart1 = new MimeBodyPart();
		messageBodyPart1.setText(msg);

		// Create Multipart object and add MimeBodyPart objects to this object
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart1);

		// Create new MimeBodyPart object and set DataHandler object to this object
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		DataSource source = new FileDataSource(pdfPath + File.separator + pdf);
		messageBodyPart2.setDataHandler(new DataHandler(source));
		messageBodyPart2.setFileName(source.getName());
		multipart.addBodyPart(messageBodyPart2);

		// Set the multiplart object to the message object
		message.setContent(multipart);

		// Send message
		Transport.send(message);
		//System.out.println("Email sent to " + to);
	}
}