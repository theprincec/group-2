package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
	
	
	private BigDecimal accountBalance; 
	private long accountID;
	private long UserID;
	
	
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	public long getAccountID() {
		return accountID;
	}
//	public void setAccountID(int accountID) {
//		this.accountID = accountID;
//	}
	public long getUserID() {
		return UserID;
	}
//	public void setUserID(int userID) {
//		UserID = userID;
//	}
//	
	
	
	

}
