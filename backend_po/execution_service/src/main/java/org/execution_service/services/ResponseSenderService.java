package org.execution_service.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.execution_service.DTO.ExecutionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.*;

@Service
public class ResponseSenderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService sendingExecutor = Executors.newSingleThreadExecutor();
    private final BlockingDeque<SendingResponseTask> sendingQueue = new LinkedBlockingDeque<>();


    @PostConstruct
    public void startProcessingQueue() {
        sendingExecutor.submit(() -> {
            while (true) {
                try {
                    SendingResponseTask task = sendingQueue.take();
                    sendResponseQueued(task.getExecutionResponse(), task.getResponseUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void sendResponse(ExecutionResponse executionResponse, String responseUrl){
        sendingQueue.add(new SendingResponseTask(executionResponse, responseUrl));
    }

    private void sendResponseQueued(ExecutionResponse executionResponse, String responseUrl) {
        try {
            URI uri = new URI(responseUrl);
            if (!uri.isAbsolute()) {
                System.err.println("URI is invalid: " + responseUrl);
            }else {
                restTemplate.postForEntity(uri, executionResponse, String.class);
                System.out.println("Successfully sended response on url: "+responseUrl);
            }
        } catch (Exception e) {
            System.err.println("error while response processing " + e.getMessage() + "\n" + responseUrl + "\n");
        }
    }

    @PreDestroy
    public void stopProcessingQueue() {
        sendingExecutor.shutdown();
    }


    private static class SendingResponseTask{
        private final ExecutionResponse executionResponse;
        private final String responseUrl;


        public SendingResponseTask(ExecutionResponse executionResponse, String responseUrl) {
            this.executionResponse = executionResponse;
            this.responseUrl = responseUrl;
        }

        public String getResponseUrl() {
            return responseUrl;
        }

        public ExecutionResponse getExecutionResponse() {
            return executionResponse;
        }

    }
}
