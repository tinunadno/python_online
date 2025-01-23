package org.session_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class RequestSendingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JWTService jwtService;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public RequestSendingService(DiscoveryClient discoveryClient, JWTService jwtService) {
        this.discoveryClient = discoveryClient;
        this.jwtService = jwtService;
    }

    private String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("Service not found: " + serviceName);
        }
        return instances.get(0).getUri().toString();
    }

    public ResponseEntity<Map> sendPostRequestAsMap(String serviceName, String endpoint, Map<String, String> request){
        String serviceUrl = getServiceUrl(serviceName) + endpoint;

        String jwtToken = jwtService.generateToken("proxy post request", serviceName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);


        return restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, Map.class);
    }

    public ResponseEntity<Map> sendPostRequest(String serviceName, String endpoint, Object request){

        String serviceUrl = getServiceUrl(serviceName) + endpoint;

        Map<String, String> requestBody = null;

        if(request != null){
            requestBody  = objectMapper.convertValue(request, Map.class);
        }

        String jwtToken = jwtService.generateToken("fileServicePostRequest", serviceName);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, Map.class);
    }
}

