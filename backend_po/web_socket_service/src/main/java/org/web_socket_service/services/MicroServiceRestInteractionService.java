package org.web_socket_service.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MicroServiceRestInteractionService {
    private final RestTemplate restTemplate = new RestTemplate();


    public ResponseEntity<Map> sendPostRequest(String url, Object request){
        return restTemplate.postForEntity(url, request, Map.class);
    }

    public ResponseEntity<Map> sendGetRequest(String url) {
        return restTemplate.getForEntity(url, Map.class);
    }
}
