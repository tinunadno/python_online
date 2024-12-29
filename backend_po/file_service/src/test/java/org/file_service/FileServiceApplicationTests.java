package org.file_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void CreateFileTestNormalBehavior() {
		ResponseEntity<Map> response = restTemplate.postForEntity(
				"FileAPI/createNewFile",
				null,
				Map.class);
	}

}
