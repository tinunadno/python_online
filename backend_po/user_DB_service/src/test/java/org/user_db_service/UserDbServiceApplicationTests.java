package org.user_db_service;

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
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserDbServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

	@Test
	void authenticationControllerNormalBehaviorTest() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername");
		request.put("email", "asd@mail.ru");
		request.put("password", "12345678");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		request = new HashMap<>();
		request.put("username", "testUsername");
		request.put("password", "12345678");

		response = testRestTemplate.postForEntity(
				"/Authentication/authorize",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void authenticationControllerRepeatName() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername");
		request.put("email", "asd@mail.ru");
		request.put("password", "12345678");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("Username already exists", response.getBody().get("message"));
	}

	@Test
	void authenticationControllerWrongPasswordTest() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername1");
		request.put("email", "asd@mail.ru");
		request.put("password", "12345678");


		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		request = new HashMap<>();
		request.put("username", "testUsername");
		request.put("password", "123456789");

		response = testRestTemplate.postForEntity(
				"/Authentication/authorize",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("Invalid password", response.getBody().get("message"));
	}

	@Test
	void authenticationControllerWrongEmailTest() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername3");
		request.put("email", "asd-poop");
		request.put("password", "12345678");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("email must be valid", response.getBody().get("message"));
	}

	@Test
	void authenticationControllerWrongParametersTest() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("usernameasd", "testUsername3");
		request.put("emaildsa", "asd@poop");
		request.put("passwordasd", "12345678");
		request.put("passwordasd2", "12345678");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	void authenticationControllerSomethingIsRequiredTest() throws Exception {
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsernam34");
		request.put("email", "asd@poop");
		request.put("password", "");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("Password length must be at least 8 characters", response.getBody().get("message"));

		request = new HashMap<>();
		request.put("username", "testUsername3");
		request.put("email", "");
		request.put("password", "12345678");

		response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("email is required", response.getBody().get("message"));

		request = new HashMap<>();
		request.put("username", "");
		request.put("email", "yura@mail.ru");
		request.put("password", "12345678");

		response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("user name is required", response.getBody().get("message"));

		request = new HashMap<>();
		request.put("username", "");
		request.put("password", "12345678");

		response = testRestTemplate.postForEntity(
				"/Authentication/authorize",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("user name is required", response.getBody().get("message"));

		request = new HashMap<>();
		request.put("username", "asdasd");
		request.put("password", "12");

		response = testRestTemplate.postForEntity(
				"/Authentication/authorize",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("Password length must be at least 8 characters", response.getBody().get("message"));
	}

	@Test
	void deleteUserTestNormalBehaviour(){
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername5");
		request.put("email", "asd@mail.ru");
		request.put("password", "12345678");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		response = testRestTemplate.postForEntity(
				"/userManagement/deleteUser",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void deleteUserTestWrongPassword(){
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername5");
		request.put("email", "asd@mail.ru");
		request.put("password", "12345678");

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/Authentication/register",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		request.put("password", "1233333333333333");

		response = testRestTemplate.postForEntity(
				"/userManagement/deleteUser",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Passwords do not match", response.getBody().get("message"));

		request.put("password", "12345678");

		response = testRestTemplate.postForEntity(
				"/userManagement/deleteUser",
				request,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void deleteUserTestWrongUserName(){
		Map<String, String> request = new HashMap<>();
		request.put("username", "testUsername5");
		request.put("password", "12345678");


		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/userManagement/deleteUser",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("User with this name doesn't exist", response.getBody().get("message"));

	}

	@AfterAll
	static void cleanup(@Autowired DataSource dataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("cleanup.sql"));
		populator.execute(dataSource);
	}

}
