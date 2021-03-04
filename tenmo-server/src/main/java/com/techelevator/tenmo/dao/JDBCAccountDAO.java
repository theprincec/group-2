package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
	public String transfer(int senderUserID, int recipientUserID, BigDecimal amount) {
		//make sure sender has enough balance
		BigDecimal currentBalance = displayBalance(senderUserID);
		BigDecimal recipientBalance = displayBalance(recipientUserID);
		if(currentBalance.doubleValue() >= amount.doubleValue()) {
			//subrtact from sender
			String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
			jdbcTemplate.update(sql, currentBalance.subtract(amount), senderUserID);
			//add to recipient
			sql = "UPDATE account SET balance = ? WHERE user_id = ?";
			jdbcTemplate.update(sql, recipientBalance.add(amount), recipientUserID);
			//insert into transfers
			sql = "INSERT INTO transfers VALUES (DEFAULT, 2, 2, ?, ?, ?)";
			//jdbcTemplate.update(sql, currentUserID, )
			return null;  
		}
	}

}

