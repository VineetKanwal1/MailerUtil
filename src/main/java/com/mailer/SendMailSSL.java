package com.mailer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SendMailSSL {
	public static void main(String[] args) {
		
		System.out.println("Usage:");
		System.out.println("java -jar MailerUtil.jar <<from-gmail-address>> <<gmail-password>> <<subject>> <<pdfs-path>>");
		System.out.println("Please note that all the pdfs in maillist.txt should be present in the pdf path given.");
		System.out.println("This utility expects maillist.txt and mailbody.txt in same folder as MailerUtil.jar");
		
		if(args.length < 4) {
			System.out.println("Please check usage..");
			System.exit(1);
		}
		String from = args[0];
		String password = args[1];
		String subject = args[2];
		String pdfPath = args[3];
		
		String mailList = "maillist.txt";
		String mailbody = "mailbody.txt";
		StringBuilder body = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(mailbody))) {

            stream.forEach(s -> {
            	body.append(s + "\n");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(mailList))) {

            stream.forEach(s -> {
            	String[] parts = s.split(":");
            	String to = parts[0];
            	String pdfs = parts[1];
            	Mailer.send(from, password, to, subject, body.toString(), pdfs, pdfPath);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }		
	}
}