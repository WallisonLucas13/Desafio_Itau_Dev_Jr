package com.example.desafio.itau.repositories;

import com.example.desafio.itau.models.TransactionModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
@Repository
public class TransactionRepository {

    /**
     * Lista thread-safe para armazenar modelos de transações.
     * A lista é inicializada como uma CopyOnWriteArrayList para garantir
     * acesso e modificações seguras em um ambiente concorrente.
     */
    private final List<TransactionModel> transactions = new CopyOnWriteArrayList<>();

    public void save(TransactionModel transaction){
        log.info("Salvando transação...");
        transactions.add(transaction);
    }

    public List<TransactionModel> findAll(){
        log.info("Buscando todas as transações...");
        return Collections.unmodifiableList(transactions);
    }

    public void deleteAll(){
        log.info("Deletando todas as transações...");
        transactions.clear();
    }
}
