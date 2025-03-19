package com.example.desafio.itau.controllers;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.models.StatisticModel;
import com.example.desafio.itau.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private static final String DEFAULT_SECONDS = "60";

    @Autowired
    private TransactionService service;

    @PostMapping("/transacao")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionDto dto){
        service.saveTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/transacao")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteTransactions(){
        service.deleteTransactions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/estatistica")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatisticModel> getStatistic(
            @RequestParam(value = "segundos", required = false, defaultValue = DEFAULT_SECONDS) int seconds
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getStatistic(seconds));
    }
}
