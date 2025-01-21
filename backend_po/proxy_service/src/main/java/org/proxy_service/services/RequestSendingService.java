package org.proxy_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import org.proxy_service.DTO.ErrorResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RequestSendingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JWTService jwtService = new JWTService();

    public ResponseEntity<?> sendPostRequestProxy(String url, Map<String, String> request){
        String jwtToken = jwtService.generateToken("proxy post request");

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        }catch(HttpStatusCodeException e){
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("current service is not available now"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map> sendPostRequest(String url, Object request){
        try {

            Map<String, String> requestBody = null;

            if(request != null){
                requestBody  = objectMapper.convertValue(request, Map.class);
            }

            String jwtToken = jwtService.generateToken("ServicePostRequest");

            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + jwtToken);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            return restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        }catch (HttpStatusCodeException e){
            throw new RuntimeException("service responsed with code " + e.getStatusCode() +"\nerror message: "+e.getMessage());
        }catch (Exception e){
            throw new RuntimeException("service is not available now");
        }
    }

    public ResponseEntity<Map> sendGetRequest(String url, Object request) {
        try {
            String jwtToken = jwtService.generateToken("ServiceGetRequest");

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
            if (request != null) {
                Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
                paramMap.forEach(uriBuilder::queryParam);
            }
            String finalUrl = uriBuilder.toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(finalUrl, HttpMethod.GET, entity, Map.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Service responded with code " + e.getStatusCode() + "\nError message: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Service is not available now");
        }
    }
}
