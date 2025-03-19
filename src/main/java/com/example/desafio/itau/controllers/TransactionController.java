package com.example.desafio.itau.controllers;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping("/transacao")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionDto dto){
        service.saveTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
