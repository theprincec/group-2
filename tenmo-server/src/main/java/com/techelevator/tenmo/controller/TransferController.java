package com.techelevator.tenmo.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
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
	
	
	@RequestMapping(path="/users/transfers", method=RequestMethod.POST)
	public Transfer makeTransfer(Principal principal, @RequestBody Transfer transfer ) {

		Transfer newTransfer = transferDAO.addTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		
		return newTransfer;
	}
	
	@RequestMapping(path="/users/transfers/requests", method=RequestMethod.POST)
	public Transfer requestTransfer(Principal principal, @RequestBody Transfer transfer ) {

		Transfer newTransfer = transferDAO.addTransferRequest(transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
		
		return newTransfer;
	}
	
	@RequestMapping(path="/users/transfers/approved/{id}", method=RequestMethod.PUT)
	public void updateTransferApprove(Principal principal, @RequestBody Transfer transfer) {

		transferDAO.updateTransferApprove(transfer.getTransferID(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		
	}
	
	@RequestMapping(path="/users/transfers/rejected/{id}", method=RequestMethod.PUT)
	public void updateTransferReject(Principal principal, @RequestBody Transfer transfer) {

		transferDAO.updateTransferReject(transfer.getTransferID());
		
	}
	
//	@RequestMapping(path="/users/transfers/requests/reject/{id}", method=RequestMethod.PUT)
//	public Transfer updateTransferReject(Principal principal, @RequestBody Transfer transfer, int id) {
//
//		Transfer requestedTransfer = transferDAO.updateTransferReject(id, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
//		
//		return requestedTransfer;
//	}
    
}
