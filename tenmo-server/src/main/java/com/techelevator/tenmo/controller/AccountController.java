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
	
	public AccountController(AccountDAO accountDAO, UserDAO userDAO) {
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
	}
		
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {

		int accountUserID = userDAO.findIdByUsername(principal.getName());
		
		BigDecimal accountBalance = accountDAO.displayBalance(accountUserID);
		
		return accountBalance;
	}

}
