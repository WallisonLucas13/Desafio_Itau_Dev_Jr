package com.example.desafio.itau.controllers;

import com.example.desafio.itau.controllers.doc.TransactionControllerSwagger;
import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.models.StatisticModel;
import com.example.desafio.itau.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TransactionController implements TransactionControllerSwagger {

    private static final String DEFAULT_SECONDS = "60";

    @Autowired
    private TransactionService service;

    @PostMapping("/transacao")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<Void> saveTransaction(@RequestBody TransactionDto dto){
        service.saveTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/transacao")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<Void> deleteTransactions(){
        service.deleteTransactions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/estatistica")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public ResponseEntity<StatisticModel> getStatistic(
            @RequestParam(value = "intervalo", required = false, defaultValue = DEFAULT_SECONDS) long timeInterval
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getStatistic(timeInterval));
    }
}
