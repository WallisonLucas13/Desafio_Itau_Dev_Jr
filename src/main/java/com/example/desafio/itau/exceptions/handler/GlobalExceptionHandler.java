package com.example.desafio.itau.exceptions.handler;

import com.example.desafio.itau.exceptions.FutureDateException;
import com.example.desafio.itau.exceptions.NegativeValueException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NegativeValueException.class, FutureDateException.class})
    public ResponseEntity<Void> handleTransactionInvalidException(Exception e) {
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonParseException.class, JsonMappingException.class})
    public ResponseEntity<Void> handleJsonBodyInvalidException(Exception e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
