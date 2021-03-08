package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.APIUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
public class AccountController {
	
	private AccountDAO accountDAO;
	private UserDAO userDAO;
	private TransferDAO transferDAO;
	
	private int userID;

	public AccountController(AccountDAO accountDAO, UserDAO userDAO, TransferDAO transferDAO) {
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
		this.transferDAO = transferDAO;
	}
	
	@RequestMapping(path="/users/balance", method=RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {

		userID = findUserID(principal);
		
		BigDecimal accountBalance = accountDAO.getBalance(userID);
		
		return accountBalance;
	}
	
	@RequestMapping(path="/users/account", method=RequestMethod.GET)
	public long getAccountNumber(@RequestParam(required=true) long id) {
		long accountID = accountDAO.getAccountId(id);
		return accountID;
	}
	
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public List<APIUser> getAllUsers() {
		List<APIUser> apiUsers = userDAO.convertToAPIUsers();
		return apiUsers;
	}
		
	
	@RequestMapping(path="/users/transfers", method=RequestMethod.GET)
	public List<Transfer> getTransferList(Principal principal/*, @RequestParam(required=false) int status*/) {

		List<Transfer> transferList = new ArrayList<Transfer>();
		
		userID = findUserID(principal);
		long accountId = accountDAO.getAccountId(userID);
//		if (status == 1) {
//			transferList = transferDAO.getPendingTransferList(findUserID(principal));
//		} else if (status == 2) {
//			transferList = transferDAO.getCompletedTransferList(findUserID(principal));
//		} else {
			transferList = transferDAO.getFullTransferList(accountId);
		//}
		
		return transferList;
	}
	
	
//	@RequestMapping(path = "/hotels/filter", method = RequestMethod.GET)
//    public List<Hotel> filterByStateAndCity(@RequestParam String state, @RequestParam(required = false) String city) {

	
	
	private int findUserID(Principal principal) {
		userID = userDAO.findIdByUsername(principal.getName());
		return userID;
	}

}
