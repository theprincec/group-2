package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCAccountDAO implements AccountDAO {
	
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public BigDecimal displayBalance(int userID) {
		// TODO Auto-generated method stub
		
		String sql = "select * from accounts where user_id = ?";
		BigDecimal accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userID);
		
		return accountBalance;
	}
	

	
	
//	select balance from accounts
//	where user_id = ?;

	@Override
	public String transfer(int userID, BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

}

