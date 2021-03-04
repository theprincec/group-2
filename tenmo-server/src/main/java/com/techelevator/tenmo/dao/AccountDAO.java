package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal displayBalance(int userID);

	String send(int senderUserID, int recipientUserID, BigDecimal amount);

	List<Transfer> listTransfer();
	
}
