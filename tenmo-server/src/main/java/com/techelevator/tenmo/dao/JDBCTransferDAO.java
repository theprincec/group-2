package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Transfer;

public class JDBCTransferDAO implements TransferDAO {

	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	@Override
	public List<Transfer> getFullTransferList(int accountID) {
		// TODO Auto-generated method stub
		return getAllTransfers(accountID);
	}
	
	@Override
	public List<Transfer> getCompletedTransferList(int accountID) {
		// TODO Auto-generated method stub
		return getCompletedTransfers(accountID);
	}

	@Override
	public List<Transfer> getPendingTransferList(int accountID) {
		// TODO Auto-generated method stub
		return getPendingTransfers(accountID);
	}
	
	
	
	
	private List<Transfer> getAllTransfers(int accountID)  {
		String sql = "select * from transfers " + 
				"JOIN transfer_types ON transfers.transfer_type_id = transfers.transfer_type_id " + 
				"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id"
				+ "Where transfers.account_from = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		List<Transfer> transfersList = new ArrayList<Transfer>();
		
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfersList.add(transfer);
		}
		
		
		return transfersList;
	}
	
	
	private List<Transfer> getPendingTransfers(int accountID)  {
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
	
	private List<Transfer> getCompletedTransfers(int accountID)  {
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
	

}
