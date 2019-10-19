package com.example.restapi_service.controller;

import com.example.restapi_service.dto.BaseResponseDto;
import com.example.restapi_service.dto.StatusCode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class BaseController {

    @Autowired
    public HttpServletRequest request;


    protected ResponseEntity<Object> returnErrorResponse() {
        BaseResponseDto response = BaseResponseDto.builder()
                .statusCode(StatusCode.FAILED)
                .message("There was an error")
                .allErrors(Arrays.asList("There was an error"))
                .build();
        return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
    }

    private List<ClientHttpRequestInterceptor> getInterceptors() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        final String remoteAddr = request.getRemoteAddr();
        log.info("ip address is {}", remoteAddr);
        interceptors.add((request, body, execution) -> {
            request.getHeaders().set("client_ip_address", remoteAddr);
            return execution.execute(request, body);
        });
        return interceptors;
    }

    protected ResponseEntity<Object> getPOSTApiResponse(RestTemplate restTemplate, String uri, Object dto, Class<?> responseType) {
        try {
            restTemplate.setInterceptors(getInterceptors());
            Object result = restTemplate.postForObject(uri, dto, Object.class);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            String exceptionBody = e.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            Object errorResult = null;
            try {
                errorResult = objectMapper.readValue(exceptionBody, Object.class);
            } catch (JsonParseException e1) {
                // TODO Auto-generated catch block
                log.error("Exception: ", e1);
            } catch (JsonMappingException e1) {
                // TODO Auto-generated catch block
                log.error("Exception: ", e1);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                log.error("Exception: ", e1);
            }
            return new ResponseEntity<Object>(errorResult, HttpStatus.BAD_REQUEST);
        }

    }

    protected ResponseEntity<Object> getGETApiResponse(RestTemplate restTemplate, String uri, Class<?> responseType) {
        restTemplate.getInterceptors();
        try {
            Object result = restTemplate.getForObject(uri, Object.class);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            String exceptionBody = e.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            Object errorResult = null;
            try {
                errorResult = objectMapper.readValue(exceptionBody, Object.class);
            } catch (JsonParseException e1) {
                // TODO Auto-generated catch block
                log.error("Exception: ", e1);
            } catch (JsonMappingException e1) {
                // TODO Auto-generated catch block
                log.error("Exception: ", e1);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                log.error("Exception: ", e1);
            }
            return new ResponseEntity<Object>(errorResult, HttpStatus.BAD_REQUEST);
        }

    }

    protected ResponseEntity<Object> getDefaultErrorResponse(String message) {
        return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
    }




}
