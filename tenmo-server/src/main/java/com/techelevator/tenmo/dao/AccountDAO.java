package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal displayBalance(long userID);

	String send(long senderUserID, long recipientUserID, BigDecimal amount);

	long getAccountId(long userID);

}
