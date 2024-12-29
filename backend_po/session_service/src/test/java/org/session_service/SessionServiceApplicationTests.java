package org.session_service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void createSessionTestNormalBehavior() {
		Map<String, String> request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionFileId", "111111111111111111111111");

		ResponseEntity<Map> response =  restTemplate.postForEntity(
				"/sessionAPI/createSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void createSessionTestTooSmallFileDescriptor() {
		Map<String, String> request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionFileId", "1111111111");

		ResponseEntity<Map> response =  restTemplate.postForEntity(
				"/sessionAPI/createSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertEquals(response.getBody().get("message"), "file id length must be 24 characters");
	}

	@Test
	void joinSessionTestNormalBehavior() {
		Map<String, String> request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionFileId", "111111111111111111111112");

		ResponseEntity<Map> response =  restTemplate.postForEntity(
				"/sessionAPI/createSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.OK);

		String sessionId = response.getBody().get("sessionId").toString();

		String url = "/sessionAPI/joinSession?sessionId=" + sessionId;

		response = restTemplate.getForEntity(url, Map.class);


		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().get("sessionId"), sessionId);
	}

	@Test
	void joinSessionTestWrongSessionId(){
		String url = "/sessionAPI/joinSession?sessionId=123456";

		ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertEquals(response.getBody().get("message"), "session with this id doesn't exist");
	}

	@Test
	void deleteSessionTestNormalBehavior() {
		Map<String, String> request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionFileId", "111111111111111111111113");

		ResponseEntity<Map> response =  restTemplate.postForEntity(
				"/sessionAPI/createSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.OK);

		String sessionId = response.getBody().get("sessionId").toString();

		request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionId", sessionId);

		response =  restTemplate.postForEntity(
				"/sessionAPI/deleteSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	void deleteSessionTestSessionAndUserNotExist(){
		Map<String, String> request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionId", "111111");

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/sessionAPI/deleteSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertEquals(response.getBody().get("message"), "session with this id doesn't exist");
	}

	@Test
	void deleteSessionTestSessionNotExist(){
		Map<String, String> request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionFileId", "111111111111111111111114");

		ResponseEntity<Map> response =  restTemplate.postForEntity(
				"/sessionAPI/createSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.OK);


		request = new HashMap<>();
		request.put("userId", "123456");
		request.put("sessionId", "111111");

		response = restTemplate.postForEntity(
				"/sessionAPI/deleteSession",
				request,
				Map.class
		);

		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertEquals(response.getBody().get("message"), "session with this id doesn't exist");

	}

	@AfterAll
	static void cleanup(@Autowired DataSource dataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("cleanup.sql"));
		populator.execute(dataSource);
	}

}
