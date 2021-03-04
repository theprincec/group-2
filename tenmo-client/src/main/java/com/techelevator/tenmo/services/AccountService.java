package com.techelevator.tenmo.services;


import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;



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
	
}
