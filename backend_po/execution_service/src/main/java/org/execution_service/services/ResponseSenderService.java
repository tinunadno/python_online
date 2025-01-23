package org.execution_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.execution_service.DTO.ExecutionResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ResponseSenderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService sendingExecutor = Executors.newSingleThreadExecutor();
    private final BlockingDeque<SendingResponseTask> sendingQueue = new LinkedBlockingDeque<>();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostConstruct
    public void startProcessingQueue() {
        sendingExecutor.submit(() -> {
            while (true) {
                try {
                    SendingResponseTask task = sendingQueue.take();
                    sendResponseQueued(task.getExecutionResponse(), task.getResponseUrl(), task.getCallbackToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @PreDestroy
    public void stopProcessingQueue() {
        sendingExecutor.shutdown();
    }


    public void sendResponse(ExecutionResponse executionResponse, String responseUrl, String callbackToken){
        sendingQueue.add(new SendingResponseTask(executionResponse, responseUrl, callbackToken));
    }

    private void sendResponseQueued(ExecutionResponse executionResponse, String responseUrl, String callbackToken) {
        try {

            Map<String, String> requestBody = objectMapper.convertValue(executionResponse, Map.class);

            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + callbackToken);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            URI uri = new URI(responseUrl);
            if (!uri.isAbsolute()) {
                System.err.println("URI is invalid: " + responseUrl);
            }else {
                System.out.println("Successfully sended response on url: "+responseUrl);
                restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
            }
        } catch (Exception e) {
            System.err.println("error while response processing " + e.getMessage() + "\n" + responseUrl + "\n");
        }
    }


    private static class SendingResponseTask{
        private final ExecutionResponse executionResponse;
        private final String responseUrl;
        private final String callbackToken;

        public SendingResponseTask(ExecutionResponse executionResponse, String responseUrl, String callbackToken) {
            this.executionResponse = executionResponse;
            this.responseUrl = responseUrl;
            this.callbackToken = callbackToken;
        }

        public String getResponseUrl() {
            return responseUrl;
        }

        public ExecutionResponse getExecutionResponse() {
            return executionResponse;
        }

        public String getCallbackToken() {
            return callbackToken;
        }
    }
}
