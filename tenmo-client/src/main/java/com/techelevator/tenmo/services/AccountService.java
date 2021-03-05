package com.techelevator.tenmo.services;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
		
		BigDecimal balance = restTemplate.exchange(baseUrl + "users/balance", HttpMethod.GET, entity, BigDecimal.class).getBody();
		
		return balance;
	}
	
	//finding current userID
	public int getUserID() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity(headers);
		
		int id = restTemplate.exchange(baseUrl + "users/", HttpMethod.GET, entity, Integer.class).getBody();
		
		return id;
	}
	
	
	//deserializing list of string users
	public List<User> getListOfUsers(){
		HttpHeaders headers = new HttpHeaders();
		//headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity(headers);

		User[] userResult = restTemplate.getForObject(baseUrl + "users/", User[].class);
		
		return Arrays.asList(userResult);
	}
	
	public List<Transfer> getListOfTransfers(){
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity(headers);
		
		Transfer[] transfers = restTemplate.exchange(baseUrl + "users/transfers", HttpMethod.GET, entity, Transfer[].class).getBody();
		
		return Arrays.asList(transfers);
	}

	public Transfer sendTransfer(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity<Transfer> entity = new HttpEntity(transfer, headers);
		
		Transfer newTransfer = restTemplate.exchange(baseUrl + "users/transfers/send", HttpMethod.POST, entity, Transfer.class).getBody();
		
		return newTransfer;
	}
	
	
}
