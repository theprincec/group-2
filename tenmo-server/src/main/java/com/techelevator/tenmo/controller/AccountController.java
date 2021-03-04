package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.UserDAO;

@RestController
public class AccountController {
	
	private AccountDAO accountDAO;
	private UserDAO userDAO;
	private int accountUserID;
	
	
	public AccountController(AccountDAO accountDAO, UserDAO userDAO) {
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
	}
	
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {

		accountUserID = findUserID(principal);
		
		BigDecimal accountBalance = accountDAO.displayBalance(accountUserID);
		
		return accountBalance;
	}
	
	@RequestMapping(path="/users/transfers/send", method=RequestMethod.POST)
	public String makeTransfer(Principal principal, int recipientUserID, BigDecimal amount) {

		accountUserID = findUserID(principal);
		
		String status = accountDAO.send(accountUserID, recipientUserID, amount);
		
		return status;
	}
	
	@RequestMapping(path="/users/transfers", method=RequestMethod.GET)
	
	
	private int findUserID(Principal principal) {
		accountUserID = userDAO.findIdByUsername(principal.getName());
		return accountUserID;
	}
	
}
