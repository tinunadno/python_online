package org.session_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RequestSendingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ResponseEntity<Map> sendPostRequestAsMap(String url, Map<String, String> request){
        return restTemplate.postForEntity(url, request, Map.class);
    }

    public ResponseEntity<Map> sendPostRequest(String url, Object request){

        Map<String, Object> requestBody = null;

        if(request != null){
            requestBody  = objectMapper.convertValue(request, Map.class);
        }

        return restTemplate.postForEntity(url, requestBody, Map.class);
    }
}

