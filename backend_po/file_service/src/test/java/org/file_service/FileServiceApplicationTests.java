package org.file_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FileServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void createFileTestNormalBehavior() {
		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/createNewFile",
				null,
				Map.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	@Test
	void deleteFileTestNormalBehavior() {
		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/createNewFile",
				null,
				Map.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		String fileId = response.getBody().get("fileId").toString();

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("fileId", fileId);

		response = restTemplate.postForEntity(
				"/fileAPI/deleteFile",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void deleteFileTestInvalidId(){
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("fileId", "wrongone eheh");

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/deleteFile",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid ObjectId format", response.getBody().get("message"));
	}

	@Test
	void deleteFileTestFileDoesntExist(){
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("fileId", "6771cdbfba9aae3dcd6bdabb");

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/deleteFile",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("file with this id doesn't exist", response.getBody().get("message"));
	}

	@Test
	void getFileContentTestFileDoesntExist(){

		ResponseEntity<Map> response = restTemplate.getForEntity("/fileAPI/getFileContent?fileId=6771cdbfba9aae3dcd6bdabb", Map.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("file with this id doesn't exist", response.getBody().get("message"));
	}

	@Test
	void updateFileContentTestInvalidId(){
		Map<String, String> requestBody = new HashMap<>();
		String testContent = "test content";
		requestBody.put("fileId", "asd");
		requestBody.put("content", testContent);

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/updateFileContent",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Invalid file id format", response.getBody().get("message"));
	}

	@Test
	void updateFileContentTestFileIdDoesntExist(){
		Map<String, String> requestBody = new HashMap<>();
		String testContent = "test content";
		requestBody.put("fileId", "6771cdbfba9aae3dcd6bdabb");
		requestBody.put("content", testContent);

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/updateFileContent",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("file with this id doesn't exist", response.getBody().get("message"));
	}

	@Test
	void updateFileContentTestNoFileIdDefined(){
		Map<String, String> requestBody = new HashMap<>();
		String testContent = "test content";
		requestBody.put("content", testContent);

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/updateFileContent",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("file id is required", response.getBody().get("message"));
	}

	@Test
	void updateFileContentTestNoContentDefined(){
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("fileId", "6771cdbfba9aae3dcd6bdabb");

		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/updateFileContent",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("content is required", response.getBody().get("message"));
	}

	@Test
	void updateFileContentAndGetFileContentTestNormalBehavior(){
		ResponseEntity<Map> response = restTemplate.postForEntity(
				"/fileAPI/createNewFile",
				null,
				Map.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		String fileId = response.getBody().get("fileId").toString();

		Map<String, String> requestBody = new HashMap<>();
		String testContent = "test content";
		requestBody.put("fileId", fileId);
		requestBody.put("content", testContent);

		response = restTemplate.postForEntity(
				"/fileAPI/updateFileContent",
				requestBody,
				Map.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		response = restTemplate.getForEntity("/fileAPI/getFileContent?fileId=" + fileId, Map.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(testContent, response.getBody().get("content"));
	}

}
