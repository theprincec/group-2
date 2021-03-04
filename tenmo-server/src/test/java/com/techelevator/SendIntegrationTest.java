package com.techelevator;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.AccountDAO;


public class SendIntegrationTest {

//	private AccountDAO dao;
//	private JdbcTemplate jdbcTemplate;
//	private static SingleConnectionDataSource dataSource;
//	private int testUserId;
//	private int testGenreId;
//	private final static String TEST_USER_NAME = "test";
//	
//	@BeforeClass
//	public static void setupDataSource() {
//		dataSource = new SingleConnectionDataSource();
//		dataSource.setUrl("jdbc:postgresql://localhost:5432/books");
//		dataSource.setUsername("postgres");
//		dataSource.setPassword("postgres1");
//
//		dataSource.setAutoCommit(false);
//	}
//	
//	@AfterClass
//	public static void closeDataSource() throws SQLException {
//		dataSource.destroy();
//	}
//	
//	@After
//	public void rollback() throws SQLException {
//		dataSource.getConnection().rollback();
//	}
//	
//	@Before
//	public void setup() {
//		jdbcTemplate = new JdbcTemplate(dataSource);
//		dao = new JdbcBookDao(jdbcTemplate);
//		testUserId = createTestUser();
//		testGenreId = createTestGenre();
//		
//	}
//	
//	@Test
//	public void get_books_by_user() {
//		int bookIdOne = createTestBook("BookOne", testUserId, testGenreId);
//		int BookIdTwo = createTestBook("BookTwo", testUserId, testGenreId);
//		
//		List<Book> books = dao.getListByUsername(TEST_USER_NAME);
//		
//		Assert.assertEquals(2, books.size());
//		Assert.assertEquals(1, books.get(0).getGenres().size());
//	}
	
}
