package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	List<Transfer> getCompletedTransferList(int accountID);
	List<Transfer> getPendingTransferList(int accountID);

}
