package com.example.api.transaction.services;

import com.example.api.transaction.repositories.TransactionRepository;
import com.example.api.transaction.dtos.TransactionDto;
import com.example.api.transaction.exceptions.FutureDateException;
import com.example.api.transaction.exceptions.NegativeValueException;
import com.example.api.transaction.models.StatisticModel;
import com.example.api.transaction.models.TransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
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

    public StatisticModel getStatistic(long timeInterval){
        List<TransactionModel> transactions = this.getTransactionsWithLimitSeconds(timeInterval);
        log.info("Calculando estatisticas...");

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

    private List<TransactionModel> getTransactionsWithLimitSeconds(long timeInterval){
        OffsetDateTime nowMinusSeconds = OffsetDateTime.now().minusSeconds(timeInterval);
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
