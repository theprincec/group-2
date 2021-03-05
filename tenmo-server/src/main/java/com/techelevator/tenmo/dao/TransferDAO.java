package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	String addTransfer(long senderUserID, long recipientUserID, BigDecimal amount);
	
	List<Transfer> getCompletedTransferList(long accountID);
	List<Transfer> getPendingTransferList(long accountID);
	List<Transfer> getFullTransferList(long accountID);

}
