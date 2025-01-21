package org.web_socket_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class MicroServiceRestInteractionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JWTService jwtService = new JWTService();

    public ResponseEntity<Map> sendPostRequest(String url, Object request){

        Map<String, String> requestBody = null;

        if(request != null){
            requestBody  = objectMapper.convertValue(request, Map.class);
        }

        String jwtToken = jwtService.generateToken("fileServicePostRequest");

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
    }

    public ResponseEntity<Map> sendGetRequest(String url) {
        String jwtToken = jwtService.generateToken("ServiceGetRequest");

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        String finalUrl = uriBuilder.toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Map.class);
    }
}
