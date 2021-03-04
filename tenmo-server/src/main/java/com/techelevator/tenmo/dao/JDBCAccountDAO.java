package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCAccountDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public BigDecimal displayBalance(int userID) {
		
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";
		BigDecimal accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userID);
		
		return accountBalance;
	}
	
	@Override
	public String send(int senderUserID, int recipientUserID, BigDecimal amount) {
		//make sure sender has enough balance
		BigDecimal currentBalance = displayBalance(senderUserID);
		BigDecimal recipientBalance = displayBalance(recipientUserID);
		int transferStatusID = 0;
		
		//get currentAccountID
		String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
		int senderAccountID = jdbcTemplate.queryForObject(sql, Integer.class, senderUserID);
		
		//get recipientAccountID
		sql = "SELECT account_id FROM accounts WHERE user_id = ?";
		int recipientAccountID = jdbcTemplate.queryForObject(sql, Integer.class, senderUserID);
		
		if(currentBalance.doubleValue() >= amount.doubleValue()) {
			//subtract from sender
			sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
			jdbcTemplate.update(sql, currentBalance.subtract(amount), senderUserID);
			//add to recipient
			sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
			jdbcTemplate.update(sql, recipientBalance.add(amount), recipientUserID);
			
			//insert into transfers
			sql = "INSERT INTO transfers VALUES (DEFAULT, 2, 2, ?, ?, ?)";
			jdbcTemplate.update(sql, senderAccountID, recipientAccountID, amount);
			transferStatusID = 2;
		} else {
			sql = "INSERT INTO transfers VALUES (DEFAULT, 2, 3, ?, ?, ?)";
			jdbcTemplate.update(sql, senderAccountID, recipientAccountID, amount);
			transferStatusID = 3;
		}
		
		sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
		String transferStatusDescription = jdbcTemplate.queryForObject(sql, String.class, transferStatusID);
		return transferStatusDescription;
	}
	
	@Override
	public List<Transfer> listTransfer(){
		
	}
	
}

