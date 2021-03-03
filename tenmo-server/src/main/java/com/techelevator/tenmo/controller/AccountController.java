package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JDBCAccountDAO;
import com.techelevator.tenmo.dao.UserDAO;

//import com.techelevator.tenmo.dao.JDBCAccountDAO;


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
		//displayBalance(int userID);
		int accountUserID = userDAO.findIdByUsername(principal.getName());
		
		BigDecimal accountBalance = accountDAO.displayBalance(accountUserID);
		
		return accountBalance;
	}
	
	
	
	
	

}
