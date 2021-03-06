package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
    private static final boolean SEND = true;
    private static final boolean REJECT = false;

	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;


    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		accountService = new AccountService(API_BASE_URL, currentUser);
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
				accountService = new AccountService(API_BASE_URL, currentUser);
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewBalance() {
		console.printAccountBalance(accountService.getBalanceForCurrentUser());
	}
	
	private void sendBucks() {
		console.printUsers(currentUser.getUser().getId(), accountService.getListOfUsers());
		int otherID = -1;
		while(true) {
			otherID = console.sendID(currentUser.getUser().getId(), accountService.getListOfUsers(), "Enter ID of user you are sending to (0 to cancel)");
			if(otherID == 0) {
				return;
			} else {
				break;
			}
		}
		BigDecimal amount = console.getUserInputBigDecimal();
		
		Transfer transfer = new Transfer();
		transfer.setAccountFrom((int) accountService.getAccountNumberForUser(currentUser.getUser().getId()));
		transfer.setAccountTo((int) accountService.getAccountNumberForUser(otherID));
		transfer.setAmount(amount);
		
		transfer = accountService.sendTransfer(transfer);
		
		if(transfer != null) {
			console.printSuccessMessage();
		} else {
			console.printInsufficientFundsMessage();
		}
	}
	
	private void viewTransferHistory() {
		List<Transfer> validTransfers = console.printListOfSendTransfers(accountService.getAccountNumberForUser(currentUser.getUser().getId()), accountService.getListOfTransfers());
		Transfer selectedTransfer = null;
		while(true) {
			selectedTransfer = console.askForTransferID(validTransfers, "Enter transfer ID to view details (0 to cancel)");
			if(selectedTransfer == null) {
				return;
			} else {
				break;
			}
		}
		if(selectedTransfer.getUsernameFrom() == null) {
			selectedTransfer.setUsernameFrom(currentUser.getUser().getUsername());
		}
		if(selectedTransfer.getUsernameTo() == null) {
			selectedTransfer.setUsernameTo(currentUser.getUser().getUsername());
		}
		console.printTransferDetails(selectedTransfer);
	}
	
	private void requestBucks() {
		console.printUsers(currentUser.getUser().getId(), accountService.getListOfUsers());
		int otherID = -1;
		while(true) {
			otherID = console.sendID(currentUser.getUser().getId(), accountService.getListOfUsers(), "Enter ID of user you are requesting from (0 to cancel)");
			if(otherID == 0) {
				return;
			} else {
				break;
			}
		}
		BigDecimal amount = console.getUserInputBigDecimal();
		
		Transfer transfer = new Transfer();
		transfer.setAccountFrom((int) accountService.getAccountNumberForUser(otherID));
		transfer.setAccountTo((int) accountService.getAccountNumberForUser(currentUser.getUser().getId()));
		transfer.setAmount(amount);
		
		transfer = accountService.requestTransfer(transfer);
	}
	
	private void viewPendingRequests() {
		List<Transfer> requestedTransfers = console.printListOfRequestedTransfers(accountService.getAccountNumberForUser(currentUser.getUser().getId()), accountService.getListOfTransfers());
		Transfer selectedTransfer = null;
		while(true) {
			selectedTransfer = console.askForTransferID(requestedTransfers, "Enter transfer ID to approve/reject (0 to cancel)");
			if(selectedTransfer == null) {
				return;
			} else {
				break;
			}
		}
		int choice = -1;
		while(!(choice >= 0 && choice <= 2)) {
			choice = console.askRequestChoice();
		}
		if(choice == 0) {
			return;
		} else if (choice == 1) {
			BigDecimal currentBalance = accountService.getBalanceForCurrentUser();
			BigDecimal transferAmount = selectedTransfer.getAmount();
			

			if(currentBalance.doubleValue() >= transferAmount.doubleValue()) {
				accountService.updateTransfer(selectedTransfer, SEND);
				console.printSuccessMessage();
			} else {
				console.printInsufficientFundsMessage();
			}
			
		} else if (choice == 2) {
			accountService.updateTransfer(selectedTransfer, REJECT);
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+ e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
