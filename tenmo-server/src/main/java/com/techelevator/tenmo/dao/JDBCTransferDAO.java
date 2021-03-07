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
	
	
	
<<<<<<< HEAD
	
	private List<Transfer> getAllTransfers(long accountID)  {
		String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount, u.username AS username_to FROM transfers t "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = t.transfer_type_id "
				+ "JOIN transfer_statuses ON t.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "JOIN accounts a ON t.account_to = a.account_id "
				+ "JOIN users u ON a.user_id = u.user_id "
				+ "WHERE t.account_from = ?";
=======
	@Override
	public List<Transfer> getAllTransfers(long accountID)  {
		String sql = "select transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, transfer.getAmount() from transfers "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "Where transfers.account_from = ?";
>>>>>>> d39e1de991543361a7a33f7b86045588fd8c1123
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
		
		List<Transfer> transfersList = new ArrayList<Transfer>();
		
		while (results.next()) {
<<<<<<< HEAD
			Transfer transfer = mapRowToTransfer(results);
			transfer.setUsernameTo(results.getString("username_to"));
=======
			Transfer transfer = new Transfer();
			transfer = mapRowToTransfer(results);
>>>>>>> d39e1de991543361a7a33f7b86045588fd8c1123
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
	
//	@Override
//	public List<Book> getListByUsername(String name) {
//		
//		List<Book> books = new ArrayList<Book>();
//		
//		String sql = "SELECT book.id, title, author, description, cover_url, isbn, type, date_added, username " + 
//				"FROM book " + 
//				"JOIN book_user ON book_user.book_id = book.id " + 
//				"JOIN users ON book_user.user_id = users.user_id " + 
//				"WHERE username = ?";
//		
//		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
//		
//		while (results.next()) {
//			Book book = mapRowToBook(results);
//			book.setGenres( getGenresForBook(book.getId()) );
//			books.add(book);
//		}
//		
//		return books;
//	}
	
	

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
		
		int transferStatusID = 0;
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
			sql = "INSERT INTO transfers VALUES (DEFAULT, 2, 2, ?, ?, ?) Returning transfer_id";
			transferID = jdbcTemplate.queryForObject(sql, Integer.class, senderAccountID, recipientAccountID, amount);
		} else {
			return null;
		}

		sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, account_from, account_to, amount FROM transfers "
				+ "JOIN transfer_types ON transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id "
				+ "WHERE transfer_id = ?";
		SqlRowSet accountToRow = jdbcTemplate.queryForRowSet(sql, transferID);
		accountToRow.next();

		Transfer newTransfer = mapRowToTransfer(accountToRow);
		
		return newTransfer;
		
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
<<<<<<< HEAD
=======

		
		@Override
		public Transfer addTransfer(long senderUserID, long recipientUserID, BigDecimal amount) {
			
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
			
			
//			//no mapping
//			sql = "select * from transfers " + 
//					"JOIN transfer_types ON transfers.transfer_type_id = transfers.transfer_type_id " + 
//					"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id "
//					+ "Where transfers.transfer_status_id = 2 and transfers.account_from = ?";
//			
//			SqlRowSet results = jdbcTemplate.queryForRowSet(sql, senderAccountID);
			
			
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
			} 

			
			//transfer status description
//			sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
//			String transferStatusDescription = jdbcTemplate.queryForObject(sql, String.class, transferStatusID);
			
			Transfer newTransfer= new Transfer();
			
//			String sql = "select * from transfers " + 
//					"JOIN transfer_types ON transfers.transfer_type_id = transfers.transfer_type_id " + 
//					"JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id"
//					+ "Where transfers.transfer_status_id = 2 and transfers.account_from = ?";
//			
//			SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountID);
//			
			
			// getting account from details to set into transfer object. account id to get user name & user id
			String gettingAccountFromDetails = "SELECT account_id, accounts.user_id, username, balance FROM accounts " + 
					"JOIN users on users.user_id = accounts.user_id " + 
					"WHERE account_id = ?";
			SqlRowSet accountFromRow = jdbcTemplate.queryForRowSet(gettingAccountFromDetails, senderAccountID);
			accountFromRow.next();
			//newTransfer.setAccountFrom(accountFromRow.getInt("user_id"));
			newTransfer.setUsername(accountFromRow.getString("username"));
			
			// getting account to details to set into transfer object. account id to get user name & user id
			String gettingAccountToDetails = "SELECT account_id, accounts.user_id, username, balance FROM accounts " + 
					"JOIN users on users.user_id = accounts.user_id " + 
					"WHERE account_id = ?";
			SqlRowSet accountToRow = jdbcTemplate.queryForRowSet(gettingAccountToDetails, recipientAccountID);
			accountToRow.next();
			newTransfer.setUsername(accountToRow.getString("username"));

			accountToRow.next();
			newTransfer = mapRowToTransfer(accountToRow);
			newTransfer.setAccountFrom(accountToRow.getInt("account_from"));
			//newTransfer.setAmount(amount);
			newTransfer.setAccountTo(accountToRow.getInt("account_to"));

			
//			results.next();
//				Transfer newTransfer= new Transfer();
//				newTransfer = mapRowToTransfer(results);
//				newTransfer.setAccountFrom(recipientAccountID);
//				newTransfer.setAmount(amount);
//				newTransfer.setAccountTo(recipientAccountID);
//				newTransfer.setAccountFrom(recipientAccountID);
//				newTransfer.setAccountTo(recipientAccountID);
			
			return newTransfer;
			
		}

		
		
>>>>>>> d39e1de991543361a7a33f7b86045588fd8c1123
		
		
		
		
		
		
		
//		sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
//		String transferStatusDescription = jdbcTemplate.queryForObject(sql, String.class, transferStatusID);
//		return transferStatusDescription;

	
	
}
