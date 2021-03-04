package com.techelevator.tenmo.services;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;



public class AccountService {
	
	private String baseUrl;
	private AuthenticatedUser currentUser;
	private RestTemplate restTemplate = new RestTemplate();
	
	public AccountService(String baseUrl, AuthenticatedUser currentUser) {
		this.baseUrl = baseUrl;
		this.currentUser = currentUser;
	}

	public BigDecimal getBalanceForCurrentUser() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity(headers);
		
		BigDecimal balance = restTemplate.exchange(baseUrl + "users/", HttpMethod.GET, entity, BigDecimal.class).getBody();
		
		return balance;
	}
	
	public int getUserID() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity(headers);
		
		int id = restTemplate.exchange(baseUrl + "users/", HttpMethod.GET, entity, Integer.class).getBody();
		
		return id;
	}
	
	public List<User> getListOfUsers(){
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		
		User[] users = restTemplate.exchange(baseUrl + "users/transfers", HttpMethod.GET, entity, User[].class).getBody();
		
		return Arrays.asList(users);
	}
	
	public List<Transfer> getListOfTransfers(){
		HttpHeaders headers = new HttpHeaders();
		HttpEntity entity = new HttpEntity(headers);
		
		Transfer[] transfers = restTemplate.exchange(baseUrl + "users/transfers", HttpMethod.GET, entity, Transfer[].class).getBody();
		
		return Arrays.asList(transfers);
	}
}
