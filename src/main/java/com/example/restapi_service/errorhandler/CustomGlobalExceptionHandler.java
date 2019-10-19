package com.example.restapi_service.errorhandler;

import com.example.restapi_service.dto.BaseResponseDto;
import com.example.restapi_service.dto.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        if (ex instanceof AccessDeniedException){
            return handleAccesDeniedException(ex);
        }
        return hanleException(ex);
    }

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    private ResponseEntity<Object> handleAccesDeniedException(Exception ex) {
        BaseResponseDto response = BaseResponseDto.builder()
                .status("Unauthorized")
                .statusCode(StatusCode.FAILED)
                .message("Access is Denied")
                .allErrors(Arrays.asList("You are not authorized to access this resource"))
                .build();

        return new ResponseEntity<Object>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Object> hanleException(final Exception ex) {
        log.error("Exception: {}", ex.getClass().getName());
        log.error("Stack trace ", ex);
        log.error("Exception message: {}", ex.getMessage());

        //
        String errorMessage = ex.getMessage();
        BaseResponseDto response = BaseResponseDto.builder()
                .status("Failed")
                .statusCode(StatusCode.FAILED)
                .message(errorMessage)
                .allErrors(Arrays.asList(errorMessage))
                .build();

        return new ResponseEntity<Object>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
