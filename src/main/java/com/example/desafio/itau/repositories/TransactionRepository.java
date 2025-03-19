package com.example.desafio.itau.repositories;

import com.example.desafio.itau.models.TransactionModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    private static List<TransactionModel> transactions = new ArrayList<>();

    public void save(TransactionModel transaction){
        transactions.add(transaction);
    }

    public List<TransactionModel> findAll(){
        return transactions;
    }

    public void deleteAll(){
        transactions.clear();
    }
}
