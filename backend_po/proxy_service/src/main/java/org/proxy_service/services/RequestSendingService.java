package org.proxy_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.proxy_service.DTO.ErrorResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RequestSendingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ResponseEntity<?> sendPostRequestProxy(String url, Map<String, String> request, String jwtToken){
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        }catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("authentication service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map> sendPostRequest(String url, Object request){
        return restTemplate.postForEntity(url, request, Map.class);
    }

    public ResponseEntity<Map> sendGetRequest(String url, Object request) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        paramMap.forEach(uriBuilder::queryParam);

        String finalUrl = uriBuilder.toUriString();
        return restTemplate.getForEntity(finalUrl, Map.class);
    }
}
