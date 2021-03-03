package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
	
	
	private BigDecimal accountBalance; 
	private int accountID;
	private int UserID;
	
	
	public BigDecimal getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}
	public int getAccountID() {
		return accountID;
	}
//	public void setAccountID(int accountID) {
//		this.accountID = accountID;
//	}
	public int getUserID() {
		return UserID;
	}
//	public void setUserID(int userID) {
//		UserID = userID;
//	}
//	
	
	
	

}
