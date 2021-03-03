package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {
	
	BigDecimal displayBalance(int userID);
	
	String transfer(int userID, BigDecimal amount);
	
}
