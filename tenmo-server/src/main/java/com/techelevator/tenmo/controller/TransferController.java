package com.techelevator.tenmo.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
public class TransferController {

	private TransferDAO transferDAO;

	public TransferController(TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}
	
	
	@RequestMapping(path="/users/transfers/send", method=RequestMethod.POST)
	public Transfer makeTransfer(Principal principal, @RequestBody Transfer transfer ) {

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
		
		Transfer newTransfer = transferDAO.addTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		
		return newTransfer;
	}

	
}
