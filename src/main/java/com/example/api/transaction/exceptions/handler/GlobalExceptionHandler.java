package com.example.api.transaction.exceptions.handler;

import com.example.api.transaction.exceptions.FutureDateException;
import com.example.api.transaction.exceptions.NegativeValueException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NegativeValueException.class, FutureDateException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handleTransactionInvalidException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonParseException.class, JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleJsonBodyInvalidException(Exception e) {
        return new ResponseEntity<>("Json Inválido!", HttpStatus.BAD_REQUEST);
    }
}
