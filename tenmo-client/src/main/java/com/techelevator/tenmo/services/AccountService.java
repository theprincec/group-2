package com.techelevator.tenmo.services;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
		
		BigDecimal balance = restTemplate.exchange(baseUrl + "users/balance", HttpMethod.GET, entity, BigDecimal.class).getBody();
		
		return balance;
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

	public void sendTransfer(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Transfer> entity = new HttpEntity<Transfer>(transfer, headers);
		
		Transfer sendTransfer = restTemplate.exchange(baseUrl + "users/transfers/send", HttpMethod.POST, entity, Transfer.class).getBody();

		
		return sendTransfer;
	}
	
	
}
