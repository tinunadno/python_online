package org.proxy_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.proxy_service.DTO.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class RequestSendingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JWTService jwtService;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public RequestSendingService(JWTService jwtService, DiscoveryClient discoveryClient) {
        this.jwtService = jwtService;
        this.discoveryClient = discoveryClient;
    }

    public String getServiceUrl(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("Service not found: " + serviceName);
        }
        return instances.get(0).getUri().toString();
    }

    public ResponseEntity<Map> sendPostRequestProxy(String serviceName, String endpoint, Map<String, String> request){
        String serviceUrl = getServiceUrl(serviceName) + endpoint;

        String jwtToken = jwtService.generateToken("proxy post request", serviceName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        return  restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, Map.class);
    }

    public ResponseEntity<Map> sendPostRequest(String serviceName, String endpoint, Object request){

            String serviceUrl = getServiceUrl(serviceName) + endpoint;

            Map<String, String> requestBody = null;

            if(request != null){
                requestBody  = objectMapper.convertValue(request, Map.class);
            }

            String jwtToken = jwtService.generateToken("ServicePostRequest", serviceName);

            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + jwtToken);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            return restTemplate.exchange(serviceUrl, HttpMethod.POST, entity, Map.class);
    }

    public ResponseEntity<Map> sendGetRequest(String serviceName, String endpoint, Object request) {
        String jwtToken = jwtService.generateToken("ServiceGetRequest", serviceName);

        String serviceUrl = getServiceUrl(serviceName) + endpoint;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serviceUrl);
        if (request != null) {
            Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
            paramMap.forEach(uriBuilder::queryParam);
        }
        String finalUrl = uriBuilder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Map.class);
    }
}
