package com.example.desafio.itau.repositories;

import com.example.desafio.itau.models.TransactionModel;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TransactionRepository {

    private final List<TransactionModel> transactions = new CopyOnWriteArrayList<>();

    public void save(TransactionModel transaction){
        transactions.add(transaction);
    }

    public List<TransactionModel> findAll(){
        return Collections.unmodifiableList(transactions);
    }

    public void deleteAll(){
        transactions.clear();
    }
}
