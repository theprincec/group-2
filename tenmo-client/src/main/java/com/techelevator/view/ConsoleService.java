package com.techelevator.view;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
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
		Integer result = -1;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result < 0);
		return result;
	}
	
	public void printAccountBalance(BigDecimal accountBalance) {
		System.out.println("Your current account balance is: $" + String.valueOf(accountBalance) + "\n");
	}
	
	public void printUsers(int currentUserId, List<User> users) {
		System.out.println("------------------------------");
		System.out.println("Users");
		System.out.printf("%-8s %-20s \n", "ID", "Name");
		System.out.println("------------------------------");
		for(User listItem: users) {
			if(listItem.getId() != currentUserId) {
				System.out.printf("%-8s %-20s \n", listItem.getId(), listItem.getUsername());
			}
		}
		System.out.println("------------------------------");
	}
	
	public int sendID(int currentUserId, List<User> users) {
		int selectionID = 0;
		while(true) {
			selectionID = getUserInputInteger("\nEnter ID of user you are sending to (0 to cancel)");
			if(selectionID == 0) {
				return selectionID;
			}
			for(User u: users) {
				if(selectionID == u.getId() && selectionID != currentUserId) {
					return selectionID;
				}
			}
		}
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

	public List<Transfer> printListOfTransfers(long currentUserAccountID, List<Transfer> listOfTransfers) {
		List<Transfer> validTransfers = new ArrayList<Transfer>();
		System.out.println("---------------------------------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-10s %-25s %14s \n", "ID", "From/To", "Amount");
		System.out.println("---------------------------------------------------------");
		for(Transfer t : listOfTransfers) {
			//gets account #, want to get user ID
			if(t.getAccountFrom() == currentUserAccountID) {
				validTransfers.add(t);
				System.out.printf("%-10s To: %-25s   $%10s \n", t.getTransferID(), t.getUsernameTo(), String.valueOf(t.getAmount()));
			} else if (t.getAccountTo() == currentUserAccountID) {
				validTransfers.add(t);
				System.out.printf("%-10s From: %-25s $%10s \n", t.getTransferID(), t.getUsernameFrom(), String.valueOf(t.getAmount()));
			}
		}
		System.out.println("---------------------------------------------------------");
		return validTransfers;
	}

	public void printSuccessMessage() {
		System.out.println("\nTransaction completed successfully.");
	}

	public void printInsufficientFundsMessage() {
		System.out.println("\nError, insufficient funds.");
	}

	public Transfer getTransferID(List<Transfer> validTransfers) {
		Transfer transfer = null;
		int selectionID = 0;
		while(true) {
			selectionID = getUserInputInteger("\nEnter transfer ID to view details (0 to cancel)");
			if(selectionID == 0) {
				return transfer;
			}
			for(Transfer t: validTransfers) {
				if(selectionID == t.getTransferID()) {
					return t;
				}
			}
		}
	}

	public void printTransferDetails(Transfer t) {
		System.out.println("\n---------------------------------------------------------");
		System.out.println("Transfer Details");
		System.out.println("---------------------------------------------------------");
		System.out.println("Id: " + t.getTransferID());
		System.out.println("From: " + t.getUsernameFrom());
		System.out.println("To: " + t.getUsernameTo());
		System.out.println("Type: " + t.getTransferTypeDesription());
		System.out.println("Status: " + t.getTransferStatusDescription());
		System.out.println("Amount: $" + t.getAmount());
	}
	
}
