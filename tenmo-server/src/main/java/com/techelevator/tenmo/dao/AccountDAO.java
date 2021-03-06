package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface AccountDAO {
	
	BigDecimal getBalance(long userID);

	long getAccountId(long userID);

	long getUserIdFromAccount(long accountId);

}
