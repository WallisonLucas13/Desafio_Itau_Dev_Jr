package com.example.desafio.itau.services;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.models.TransactionModel;
import com.example.desafio.itau.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    public TransactionRepository repository;

    public void saveTransaction(TransactionDto dto){
        this.validateTransactionDto(dto);
        TransactionModel transactionModel = TransactionModel.builder()
                .id(UUID.randomUUID())
                .value(dto.value())
                .dateHour(dto.dateHour())
                .build();
        repository.save(transactionModel);
    }

    public void deleteTransactions(){
        repository.deleteAll();
    }

    private void validateTransactionDto(TransactionDto dto){
        if(dto.value().compareTo(new BigDecimal(0)) < 0){
            throw new IllegalArgumentException("Valor da transação não pode ser negativo");
        }
        if(dto.dateHour().isAfter(OffsetDateTime.now())){
            throw new IllegalArgumentException("Data da transação não pode ser futura");
        }
    }
}
