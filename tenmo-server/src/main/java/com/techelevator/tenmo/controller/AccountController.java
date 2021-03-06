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
	
	private int accountUserID;
	
	
	public AccountController(AccountDAO accountDAO, UserDAO userDAO, TransferDAO transferDAO) {
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
		this.transferDAO = transferDAO;
	}
	
	@RequestMapping(path="/users/balance", method=RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {

		accountUserID = findUserID(principal);
		
		BigDecimal accountBalance = accountDAO.getBalance(accountUserID);
		
		return accountBalance;
	}
	
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public List<APIUser> getAllUsers() {
		List<APIUser> apiUsers = userDAO.convertToAPIUsers();
		
//		List<User> fullList = userDAO.findAll();
//		Map<Long, String> usersByIDAndName = new HashMap<Long, String>();
//		for(User u: fullList) {
//			usersByIDAndName.put(u.getId(), u.getUsername());
//		}
		return apiUsers;
	}
	
	@RequestMapping(path="/users/transfers/send", method=RequestMethod.POST)
	public void makeTransfer(Principal principal, @Valid @RequestBody Transfer transfer ) {

		//get user account ID
//		accountUserID = findUserID(principal);
//		long userId = transfer.getAccountFrom();
//		long accountId = accountDAO.getAccountId(userId);
//		
		//get recipient account ID
//		long recipientId = transfer.getAccountFrom();
//		long recipientAccountId = accountDAO.getAccountId(recipientId);
//		
		//String status = transferDAO.addTransfer(accountId, recipientAccountId, transfer.getAmount());
		
		transferDAO.addTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		
		//return status;
	}
	
	
	@RequestMapping(path="/users/transfers", method=RequestMethod.GET)
	public List<Transfer> getTransferList(Principal principal/*, @RequestParam(required=false) int status*/) {

		List<Transfer> transferList = new ArrayList<Transfer>();
		
		accountUserID = findUserID(principal);
		long accountId = accountDAO.getAccountId(accountUserID);
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
		accountUserID = userDAO.findIdByUsername(principal.getName());
		return accountUserID;
	}
	
	
	
}
