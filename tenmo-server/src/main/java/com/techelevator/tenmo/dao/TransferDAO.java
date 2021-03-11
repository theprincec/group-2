package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	List<Transfer> getCompletedTransferList(long accountID);
	List<Transfer> getPendingTransferList(long accountID);
	List<Transfer> getFullTransferList(long accountID);
	
	Transfer addTransfer(long senderUserID, long recipientUserID, BigDecimal amount);
	Transfer addTransferRequest(int accountTo, int accountFrom, BigDecimal amount);
	Transfer updateTransferApprove(int transferID, int accountFrom, int accountTo, BigDecimal amount);
	Transfer updateTransferReject(int transferID);

}
