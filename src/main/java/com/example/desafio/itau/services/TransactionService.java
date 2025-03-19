package com.example.desafio.itau.services;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.exceptions.FutureDateException;
import com.example.desafio.itau.exceptions.NegativeValueException;
import com.example.desafio.itau.models.StatisticModel;
import com.example.desafio.itau.models.TransactionModel;
import com.example.desafio.itau.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

        CompletableFuture.runAsync(() -> repository.save(transactionModel));
    }

    public void deleteTransactions(){
        repository.deleteAll();
    }

    public StatisticModel getStatistic(int seconds){
        List<TransactionModel> transactions = this.getTransactionsWithLimitSeconds(seconds);

        if(transactions.isEmpty()) return new StatisticModel();

        DoubleSummaryStatistics statistics = transactions.parallelStream()
                .mapToDouble(transaction -> transaction.getValue().doubleValue())
                .summaryStatistics();

        return StatisticModel.builder()
                .sum(statistics.getSum())
                .avg(statistics.getAverage())
                .max(statistics.getMax())
                .min(statistics.getMin())
                .count(statistics.getCount())
                .build();
    }

    private List<TransactionModel> getTransactionsWithLimitSeconds(int seconds){
        OffsetDateTime nowMinusSeconds = OffsetDateTime.now().minusSeconds(seconds);
        return repository.findAll().parallelStream()
                .filter(transaction -> transaction.getDateHour().isAfter(nowMinusSeconds))
                .toList();
    }

    private void validateTransactionDto(TransactionDto dto){
        if(dto.value().compareTo(new BigDecimal(0)) < 0){
            throw new NegativeValueException("Valor da transação não pode ser negativo");
        }
        if(dto.dateHour().isAfter(OffsetDateTime.now())){
            throw new FutureDateException("Data da transação não pode ser futura");
        }
    }
}
