package org.execution_service;

import org.execution_service.DTO.ExecutionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
		System.out.println("\n\nnormal execution results:\n"+response+"\n\n");
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
		System.out.println("\n\ninfinite loop execution results:\n"+response+"\n\n");
	}

	@Test
	void wrongSyntaxTest(){
		String executingFile = "while(1:";

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
		System.out.println("\n\nwrong syntax execution results:\n"+response+"\n\n");
	}

	@Test
	void invalidResponseUrlTest(){
		String executingFile = "while(1:";

		Map<String, String> request = new HashMap<>();
		request.put("executableFile", executingFile);
		request.put("responseUrl", String.format("httasdlocalhost:%dtestControllersaveResponse", port));

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/executionAPI/executeFile",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("response url is invalid", response.getBody().get("message"));
	}

	@Test
	void invalidRequestTest(){

		Map<String, String> request = new HashMap<>();
		request.put("responseUrl", String.format("http://localhost:%d/testController/saveResponse", port));

		ResponseEntity<Map> response = testRestTemplate.postForEntity(
				"/executionAPI/executeFile",
				request,
				Map.class
		);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}


	void infiniteLoopTestWithOutAwait(){
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
	}

	//Just to see if server wouldn't die
	//this will be really hard for my laptop
	@Test
	void rushExecutionTest(){
		for(int i =0; i < 500; i++){
			System.out.println("running infinite loop test № "+i);
			infiniteLoopTestWithOutAwait();
		}
		//to make sure, that all the responses are sended and server will not send any responses to shutdowned test server
		try{
			Thread.sleep(10000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}

}
