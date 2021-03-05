package com.techelevator.view;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = 0;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result <= 0);
		return result;
	}
	
	public void printAccountBalance(BigDecimal accountBalance) {
		System.out.println("Your current account balance is: " + String.valueOf(accountBalance) + "\n");
	}
	
	public void printUsers(String currentUsername, List<String> usernames) {
		System.out.println("------------------------------");
		System.out.println("Users");
		System.out.printf("%-8s %-20s \n", "ID", "Name");
		System.out.println("------------------------------");
		for(String listItem: usernames) {
			if(listItem.equals(currentUsername)) {
				System.out.printf("%-8s %-20s \n", listItem);
			}
		}
		System.out.println("------------------------------");
	}
	
	public int sendID() {
		int selectionID = getUserInputInteger("\nEnter ID of user you are sending to (0 to cancel): ");
		return selectionID;
	}
	
	public BigDecimal getUserInputBigDecimal() {
		BigDecimal result = new BigDecimal(0);
		do {
			out.print("Enter amount: ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = new BigDecimal(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result.doubleValue() <= 0);
		return result;
	}

	public void printListOfTransfers(int currentUserID, List<Transfer> listOfTransfers) {
		System.out.println("------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-8s %-20s %-10s \n", "Account #", "From/To", "Amount");
		System.out.println("------------------------------");
		for(Transfer t : listOfTransfers) {
			//gets account #, want to get user ID
			if(t.getAccountFrom() == currentUserID) {
				System.out.printf("%-8s To: %-20s $%-10d \n", t.getAccountTo(), t.getAccountTo(), String.valueOf(t.getAmount()));
			} else if (t.getAccountTo() == currentUserID) {
				System.out.printf("%-8s From: %-20s $%-10d \n", t.getAccountFrom(), t.getAccountFrom(), String.valueOf(t.getAmount()));
			}
		}
		System.out.println("------------------------------");
	}



	
}
