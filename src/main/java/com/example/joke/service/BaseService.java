package com.example.joke.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Lazy
@Service
public class BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public ResponseEntity<?> getRequest (
            String path,
            HttpEntity<?> request,
            Class<?> responseType
    ) {
        logDataRequest(request);
        ResponseEntity<?> response = testRestTemplate.exchange(path, HttpMethod.GET, request, responseType);
        logDataResponse(response);
        return response;
    }

    public ResponseEntity<?> putRequest (
            String path,
            Map<String, ?> urlVariables,
            HttpEntity<?> request,
            Class<?> responseType
    ) {
        logDataRequest(request);
        ResponseEntity<?> response = testRestTemplate.exchange(
                path + paramsToRequest(urlVariables),
                HttpMethod.PUT,
                request,
                responseType,
                urlVariables
        );
        logDataResponse(response);
        return response;
    }

    public ResponseEntity<?> postRequest (
            String path,
            HttpEntity<?> request,
            Class<?> responseType
    ) {
        logDataRequest(request);
        ResponseEntity<?> response = testRestTemplate.postForEntity(path, request, responseType);
        logDataResponse(response);
        return response;
    }

    private String paramsToRequest(Map<String, ?> urlVariables) {
        StringBuilder params = new StringBuilder("?");
        for (String k : urlVariables.keySet()) {
            params.append(k).append("{").append(k).append("}&");
        }
        return params.toString();
    }

    @SneakyThrows
    private void logDataRequest(HttpEntity<?> request) {
            if (request == null) {
                logger.info("Request body: null");
                logger.info("Request headers: null");

            } else {
                logger.info("Request headers: {}", objectMapper.writeValueAsString(request.getHeaders().toString()));
                logger.info("Request body: {}", objectMapper.writeValueAsString(request.getHeaders().toString()));
            }
    }

    @SneakyThrows
    private void logDataResponse(ResponseEntity<?> response) {
        logger.info("Response headers: {}", objectMapper.writeValueAsString(response.getHeaders()));
        logger.info("Response body: {}", objectMapper.writeValueAsString(response.getBody()));
    }
}