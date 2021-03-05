package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCTransferDAO implements TransferDAO {

	
	private JdbcTemplate jdbcTemplate;
	private JDBCAccountDAO jdbcaccountDAO;
	
	public JDBCTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public List<Transfer> getFullTransferList(long accountID) {
		// TODO Auto-generated method stub
		return getAllTransfers(accountID);
	}
	
	@Override
	public List<Transfer> getCompletedTransferList(long accountID) {
		// TODO Auto-generated method stub
		return getCompletedTransfers(accountID);
	}

	@Override
	public List<Transfer> getPendingTransferList(long accountID) {
		// TODO Auto-generated method stub
		return getPendingTransfers(accountID);
	}
	
	
	
	
	private List<Transfer> getAllTransfers(long accountID)  {
		String sql = "select transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount from transfers "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "Where transfers.account_from = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		List<Transfer> transfersList = new ArrayList<Transfer>();
		
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfersList.add(transfer);
		}
		
		
		return transfersList;
	}
	
	
	private List<Transfer> getPendingTransfers(long accountID)  {
		String sql = "select * from transfers " + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfers.transfer_type_id " + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id"
				+ "Where transfers.transfer_status_id = 1 and transfers.account_from = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		List<Transfer> transfersList = new ArrayList<Transfer>();
		
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfersList.add(transfer);
		}
		
		
		return transfersList;
	}
	
	private List<Transfer> getCompletedTransfers(long accountID)  {
		String sql = "select * from transfers " + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfers.transfer_type_id " + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id"
				+ "Where transfers.transfer_status_id = 2 and transfers.account_from = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		List<Transfer> transfersList = new ArrayList<Transfer>();
		
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfersList.add(transfer);
		}
		return transfersList;
	}

	
	private Transfer mapRowToTransfer(SqlRowSet row) {
		Transfer transfer = new Transfer();
		
		transfer.setTransferID( row.getInt("transfer_id"));
		transfer.setTransferTypeDesription( row.getString("transfer_type_desc") );
		transfer.setTransferStatusDescription( row.getString("transfer_status_desc") );
		transfer.setAccountFrom( row.getInt("account_from") );
		transfer.setAccountTo( row.getInt("account_to") );
		transfer.setAmount( row.getBigDecimal("amount") );

		return transfer;
	}

	@Override
	public String addTransfer(long senderUserID, long recipientUserID, BigDecimal amount) {
		//make sure sender has enough balance
		
		BigDecimal currentBalance = jdbcaccountDAO.getBalance(senderUserID);
		BigDecimal recipientBalance = jdbcaccountDAO.getBalance(recipientUserID);
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
	
}
