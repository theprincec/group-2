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
	private AccountDAO accountDAO;
	
	public JDBCTransferDAO(JdbcTemplate jdbcTemplate, AccountDAO accountDAO) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountDAO = accountDAO;
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

	public List<Transfer> getAllTransfers(long accountID)  {
		String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount, u.username AS username_to FROM transfers t "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = t.transfer_type_id "
				+ "JOIN transfer_statuses ON t.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "JOIN accounts a ON t.account_to = a.account_id "
				+ "JOIN users u ON a.user_id = u.user_id "
				+ "WHERE t.account_from = ?";

		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		List<Transfer> transfersList = new ArrayList<Transfer>();
		
		while (results.next()) {

			Transfer transfer = mapRowToTransfer(results);
			transfer.setUsernameTo(results.getString("username_to"));

			transfersList.add(transfer);
		}
		
		sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount, u.username AS username_from FROM transfers t "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = t.transfer_type_id "
				+ "JOIN transfer_statuses ON t.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "JOIN accounts a ON t.account_from = a.account_id "
				+ "JOIN users u ON a.user_id = u.user_id "
				+ "WHERE t.account_to = ?";
		
		results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		while (results.next()) {
			Transfer transfer = mapRowToTransfer(results);
			transfer.setUsernameFrom(results.getString("username_from"));
			
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
			Transfer transfer = new Transfer();
			transfer = mapRowToTransfer(results);
			transfersList.add(transfer);
		}
		return transfersList;
	}

		
	@Override
	public Transfer addTransfer(long senderAccountID, long recipientAccountID, BigDecimal amount) {
		
		//get currentAccountID
		String sql = "SELECT user_id FROM accounts WHERE account_id = ?";
		int senderUserID = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountID);
		
		//get recipientAccountID
		sql = "SELECT user_id FROM accounts WHERE account_id = ?";
		int recipientUserID = jdbcTemplate.queryForObject(sql, Integer.class, recipientAccountID);
		
		//get balances of sender and recipient
		BigDecimal currentBalance = accountDAO.getBalance(senderUserID);
		BigDecimal recipientBalance = accountDAO.getBalance(recipientUserID);
		
		int transferID = 0;
		
		//make sure sender has enough balance
		if(currentBalance.doubleValue() >= amount.doubleValue()) {
			//subtract from sender
			sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
			jdbcTemplate.update(sql, currentBalance.subtract(amount), senderUserID);
			//add to recipient
			sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
			jdbcTemplate.update(sql, recipientBalance.add(amount), recipientUserID);
			
			//insert into transfers
			sql = "INSERT INTO transfers VALUES (DEFAULT, 2, 2, ?, ?, ?) RETURNING transfer_id";
			transferID = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountID, recipientAccountID, amount);
		} else {
			return null;
		}

		SqlRowSet accountToRow = selectTransferByID(transferID);
		accountToRow.next();

		Transfer newTransfer = mapRowToTransfer(accountToRow);
		
		return newTransfer;
		
	}
	
	@Override
	public Transfer addTransferRequest(int recipientAccountID, int senderAccountID, BigDecimal amount) {
		
		int transferID = 0;
		
		//insert into transfers
		String sql = "INSERT INTO transfers VALUES (DEFAULT, 1, 1, ?, ?, ?) RETURNING transfer_id";
		transferID = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountID, recipientAccountID, amount);

		SqlRowSet accountToRow = selectTransferByID(transferID);
		accountToRow.next();

		Transfer newTransfer = mapRowToTransfer(accountToRow);
		
		return newTransfer;
		
	}


	@Override
	public Transfer updateTransferApprove(int transferID, int senderAccountID, int recipientAccountID, BigDecimal amount) {
		//get currentAccountID
		String sql = "SELECT user_id FROM accounts WHERE account_id = ?";
		int senderUserID = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountID);
		
		//get recipientAccountID
		sql = "SELECT user_id FROM accounts WHERE account_id = ?";
		int recipientUserID = jdbcTemplate.queryForObject(sql, Integer.class, recipientAccountID);
		
		//get balances of sender and recipient
		BigDecimal currentBalance = accountDAO.getBalance(senderUserID);
		BigDecimal recipientBalance = accountDAO.getBalance(recipientUserID);

		sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		jdbcTemplate.update(sql, currentBalance.subtract(amount), senderUserID);
		//add to recipient
		sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
		jdbcTemplate.update(sql, recipientBalance.add(amount), recipientUserID);
		
		//update transfers
		sql = "UPDATE transfers SET transfer_status_id = 2 WHERE transfer_id = ?";
		jdbcTemplate.update(sql, transferID);


		SqlRowSet accountToRow = selectTransferByID(transferID);
		accountToRow.next();

		Transfer newTransfer = mapRowToTransfer(accountToRow);
		
		return newTransfer;
	}


	@Override
	public Transfer updateTransferReject(int transferID) {
		
		//update transfers
		String sql = "UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?";
		jdbcTemplate.update(sql, transferID);
		

		SqlRowSet accountToRow = selectTransferByID(transferID);
		accountToRow.next();

		Transfer newTransfer = mapRowToTransfer(accountToRow);
		
		return newTransfer;
	}
	
	
	
	private SqlRowSet selectTransferByID(int transferID) {
		String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount FROM transfers "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "WHERE transfer_id = ?";
		SqlRowSet accountToRow = jdbcTemplate.queryForRowSet(sql, transferID);
		return accountToRow;
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
