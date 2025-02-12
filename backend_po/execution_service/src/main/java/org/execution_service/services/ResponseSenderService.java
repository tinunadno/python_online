package org.execution_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.execution_service.DTO.ExecutionResponse;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ResponseSenderService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService sendingExecutor = Executors.newSingleThreadExecutor();
    private final BlockingDeque<SendingResponseTask> sendingQueue = new LinkedBlockingDeque<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DiscoveryClient discoveryClient;

    public ResponseSenderService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    private String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("Service not found: " + serviceName);
        }
        return instances.get(0).getUri().toString();
    }

    @PostConstruct
    public void startProcessingQueue() {
        sendingExecutor.submit(() -> {
            while (true) {
                try {
                    SendingResponseTask task = sendingQueue.take();
                    sendResponseQueued(task.getExecutionResponse(), task.getResponseServiceName(), task.getResponseServiceEndPoint(), task.getCallbackToken());
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


    public void sendResponse(ExecutionResponse executionResponse, String responseServiceName, String responseServiceEndpoint, String callbackToken){
        sendingQueue.add(new SendingResponseTask(executionResponse, responseServiceName, responseServiceEndpoint, callbackToken));
    }

    private void sendResponseQueued(ExecutionResponse executionResponse, String responseServiceName, String responseServiceEndpoint, String callbackToken) {
        try {

            Map<String, String> requestBody = objectMapper.convertValue(executionResponse, Map.class);

            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + callbackToken);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            String responseUrl= getServiceUrl(responseServiceName) + responseServiceEndpoint;

            URI uri = new URI(responseUrl);
            if (!uri.isAbsolute()) {
                System.err.println("URI is invalid: " + responseUrl);
            }else {
                System.out.println("Successfully sended response on url: "+responseUrl);
                restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
            }
        } catch (Exception e) {
            System.err.println("error while response processing " + e.getMessage() + "\n" + responseServiceName+responseServiceEndpoint + "\n");
        }
    }


    private static class SendingResponseTask{
        private final ExecutionResponse executionResponse;
        private final String responseServiceName;
        private final String responseServiceEndPoint;
        private final String callbackToken;

        public SendingResponseTask(ExecutionResponse executionResponse, String responseServiceName, String responseServiceEndPoint, String callbackToken) {
            this.executionResponse = executionResponse;
            this.responseServiceName = responseServiceName;
            this.responseServiceEndPoint = responseServiceEndPoint;
            this.callbackToken = callbackToken;
        }

        public String getResponseServiceName() {
            return responseServiceName;
        }

        public ExecutionResponse getExecutionResponse() {
            return executionResponse;
        }

        public String getResponseServiceEndPoint() {return responseServiceEndPoint;}

        public String getCallbackToken() {
            return callbackToken;
        }
    }
}
