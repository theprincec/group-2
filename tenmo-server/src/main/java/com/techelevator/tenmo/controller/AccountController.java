package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
public class AccountController {
	
	private AccountDAO accountDAO;
	private UserDAO userDAO;
	private TransferDAO transferDAO;
	
	private int accountUserID;
	
	
	public AccountController(AccountDAO accountDAO, UserDAO userDAO, TransferDAO transferDAO) {
		this.accountDAO = accountDAO;
		this.userDAO = userDAO;
		this.transferDAO = transferDAO;
	}
	
	@RequestMapping(path="/users/balance", method=RequestMethod.GET)
	public BigDecimal getBalance(Principal principal) {

		accountUserID = findUserID(principal);
		
		BigDecimal accountBalance = accountDAO.displayBalance(accountUserID);
		
		return accountBalance;
	}
	
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public Map<Long, String> getAllUsers() {
		List<User> fullList = userDAO.findAll();
		Map<Long, String> usersByIDAndName = new HashMap<Long, String>();
		for(User u: fullList) {
			usersByIDAndName.put(u.getId(), u.getUsername());
		}
		return usersByIDAndName;
	}
	
	@RequestMapping(path="/users/transfers/send", method=RequestMethod.POST)
	public String makeTransfer(Principal principal, int recipientUserID, BigDecimal amount) {

		accountUserID = findUserID(principal);
		
		String status = accountDAO.send(accountUserID, recipientUserID, amount);
		
		return status;
	}
	
	@RequestMapping(path="/users/transfers", method=RequestMethod.GET)
	public List<Transfer> getTransferList(Principal principal/*, @RequestParam(required=false) int status*/) {

		List<Transfer> transferList = new ArrayList<Transfer>();
		
//		if (status == 1) {
//			transferList = transferDAO.getPendingTransferList(findUserID(principal));
//		} else if (status == 2) {
//			transferList = transferDAO.getCompletedTransferList(findUserID(principal));
//		} else {
			
			transferList = transferDAO.getFullTransferList(findUserID(principal));
			
		//}
		
		return transferList;
	}
	
	
//	@RequestMapping(path = "/hotels/filter", method = RequestMethod.GET)
//    public List<Hotel> filterByStateAndCity(@RequestParam String state, @RequestParam(required = false) String city) {
//
//        List<Hotel> filteredHotels = new ArrayList<>();
//        List<Hotel> hotels = list();
//
//        // return hotels that match state
//        for (Hotel hotel : hotels) {
//
//            // if city was passed we don't care about the state filter
//            if (city != null) {
//                if (hotel.getAddress().getCity().toLowerCase().equals(city.toLowerCase())) {
//                    filteredHotels.add(hotel);
//                }
//            } else {
//                if (hotel.getAddress().getState().toLowerCase().equals(state.toLowerCase())) {
//                    filteredHotels.add(hotel);
//                }
//
//            }
//        }
//
//        return filteredHotels;
//    }
	
	
	private int findUserID(Principal principal) {
		accountUserID = userDAO.findIdByUsername(principal.getName());
		return accountUserID;
	}
	
	
	
}
