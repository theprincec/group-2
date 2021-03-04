package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {
	
	private int transferID;
	private int transferTypeID;
	private int transferStatusID;
	private int accountFrom;
	private int accountTo;
	private BigDecimal amount;
	
	
	public int getTransferID() {
		return transferID;
	}
	public void setTransferID(int transferID) {
		this.transferID = transferID;
	}
	public int getTransferTypeID() {
		return transferTypeID;
	}
	public void setTransferTypeID(int transferTypeID) {
		this.transferTypeID = transferTypeID;
	}
	public int getTransferStatusID() {
		return transferStatusID;
	}
	public void setTransferStatusID(int transferStatusID) {
		this.transferStatusID = transferStatusID;
	}
	public int getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}
	public int getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
