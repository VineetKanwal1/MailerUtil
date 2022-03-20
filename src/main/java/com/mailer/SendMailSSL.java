package com.mailer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SendMailSSL {
	public static void main(String[] args) {

		if (args.length < 5) {
			System.out.println("Not Enough arguments to run. Please check usage.");
			System.out.println("Usage:");
			System.out.println(
					"java -jar MailerUtil.jar <<from-gmail-address>> <<gmail-password>> <<as-of-date>> <<revert-by-date>> <<path>>");
			System.exit(1);
		}
		String from = args[0];
		String password = args[1];
		String asOfDate = args[2];
		String revertByDate = args[3];
		String path = args[4];

		File inputCsv = new File("input.csv");

		List<List<String>> records = new ArrayList<>();
		try (Scanner scanner = new Scanner(inputCsv);) {
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Problem in reading input csv from path " + inputCsv);
			e.printStackTrace();
			System.exit(1);
		}
		int counter = 0;
		for (List<String> rec : records) {
			if (counter++ == 0) {
				continue;
			}
			System.out.println("Processing record for company: " + rec.get(0).trim());
			String pdf = rec.get(0).trim() + ".pdf";
			String balance = rec.get(1).trim();
			String[] toEmails = rec.get(2).trim().split(";");
			String sub = "Outstanding Balance Confirmation as of " + asOfDate;
			String msg = "Dear Concern,\r\n" + "\r\n" + "Greetings from Tanmik Services..\r\n" + "\r\n"
					+ "As per our books, there is an outstanding balance of Rs." + balance + " as of " + asOfDate
					+ ", please refer to the attached ledger from our book.\r\n"
					+ "Please reconcile and let us know if there are any concerns. \r\n" + "\r\n"
					+ "Do ensure to revert by " + revertByDate
					+ ", otherwise, we will consider the amount payable as our book stands correct.\r\n" + "\r\n"					
					+ "Regards \r\n"
					+ "Gurpreet Singh Sabharwal \r\n"
					+ "9711722208 \r\n";

			try {
				Mailer.send(from, password, toEmails, sub, msg, pdf, path);
			} catch (Exception e) {
				System.out.println("Email could not be sent to " + toEmails + ", Company: " + rec.get(0)
						+ " Exception: " + e.getMessage());
			}

		}
		System.out.println("Total records processed: " + (records.size() - 1));
		System.out.println("All records processed successfuly!");
	}

	private static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}
}