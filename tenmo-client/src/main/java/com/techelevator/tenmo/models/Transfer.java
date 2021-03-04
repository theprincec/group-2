package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private int transferID;
	private String transferTypeDesription;
	private String transferStatusDescription;
	private int accountFrom;
	private int accountTo;
	private BigDecimal amount;
	
	
	public String getTransferTypeDesription() {
		return transferTypeDesription;
	}
	public void setTransferTypeDesription(String transferTypeDesription) {
		this.transferTypeDesription = transferTypeDesription;
	}
	public String getTransferStatusDescription() {
		return transferStatusDescription;
	}
	public void setTransferStatusDescription(String transferStatusDescription) {
		this.transferStatusDescription = transferStatusDescription;
	}
	public int getTransferID() {
		return transferID;
	}
	public void setTransferID(int transferID) {
		this.transferID = transferID;
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
