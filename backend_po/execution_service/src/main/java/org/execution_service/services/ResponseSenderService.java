package org.execution_service.services;

import org.execution_service.DTO.ExecutionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class ResponseSenderService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendResponse(ExecutionResponse executionResponse, String responseUrl) {
        try {
            URI uri = new URI(responseUrl);
            if (!uri.isAbsolute()) {
                System.err.println("URI is invalid: " + responseUrl);
            }else {
                restTemplate.postForEntity(uri, executionResponse, String.class);
                System.out.println("Successfully sended response on url: "+responseUrl);
                System.out.println("response body: " + executionResponse.getExecutionOutput());
            }
        } catch (Exception e) {
            System.err.println("error while response processing" + e.getMessage() + "\n\n\n\n" + responseUrl + "\n\n\n\n");
        }
    }
}
