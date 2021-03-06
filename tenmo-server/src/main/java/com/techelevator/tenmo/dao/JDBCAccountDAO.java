package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

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
	public BigDecimal getBalance(long userID) {
		
		String sql = "select balance from accounts where user_id = ?";
		BigDecimal accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userID);
		
		return accountBalance;
	}
	
	
	@Override
	public long getAccountId(long userID) {
		
		String sql = "select account_id from accounts " + 
				"where user_id = ?";
		long accountId = jdbcTemplate.queryForObject(sql, Integer.class, userID);
		
		return accountId;
	}
	
	
	
	@Override
	public long getUserIdFromAccount(long accountId) {
		String sql = "select user_id from accounts " + 
				"where account_id = ?";
		long userId = jdbcTemplate.queryForObject(sql, Integer.class, accountId);
		
		return userId;
	}
	
	
}

