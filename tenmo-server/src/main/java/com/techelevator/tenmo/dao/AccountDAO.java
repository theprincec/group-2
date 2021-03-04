package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {
	
	BigDecimal displayBalance(int userID);

	String send(int senderUserID, int recipientUserID, BigDecimal amount);
	
}
