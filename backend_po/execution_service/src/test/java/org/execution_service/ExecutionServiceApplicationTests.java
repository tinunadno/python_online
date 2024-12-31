package org.execution_service;

import org.execution_service.DTO.ExecutionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExecutionServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;


	@TestConfiguration
	static class TestConfig {
		@Bean
		public ResponseControllerTemplate responseControllerTemplate() {
			return new ResponseControllerTemplate();
		}
	}

	@RestController
	@RequestMapping("/testController")
	static class ResponseControllerTemplate{
		private final CountDownLatch latch = new CountDownLatch(1);
		private String response;

		@PostMapping("/saveResponse")
		void acceptResponse(@RequestBody ExecutionResponse executionResponse){
			response = executionResponse.getExecutionOutput();
			latch.countDown();
		}

		public String getResponse(){
			return response;
		}

		public void awaitResponse() throws InterruptedException {
			latch.await(); // Ожидание сигнала
		}

	}

	@Autowired
	private ResponseControllerTemplate responseControllerTemplate;

	@Test
	void executionTest(){
		String executingFile = "print('Hello world!')";

		Map<String, String> request = new HashMap<>();
		request.put("executableFile", executingFile);
		request.put("responseUrl", String.format("http://localhost:%d/testController/saveResponse", port));

		testRestTemplate.postForEntity(
				"/executionAPI/executeFile",
				request,
				Map.class
		);

		try {
			responseControllerTemplate.awaitResponse();
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		String response = responseControllerTemplate.getResponse();
		assertEquals("Hello world!", response);
	}

	@Test
	void infiniteLoopExecutionTest(){
		String executingFile = "while(1):" +
				"	pass";

		Map<String, String> request = new HashMap<>();
		request.put("executableFile", executingFile);
		request.put("responseUrl", String.format("http://localhost:%d/testController/saveResponse", port));

		testRestTemplate.postForEntity(
				"/executionAPI/executeFile",
				request,
				Map.class
		);

		try {
			responseControllerTemplate.awaitResponse();
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		String response = responseControllerTemplate.getResponse();
		assertEquals("Hello world!", response);
	}

}
