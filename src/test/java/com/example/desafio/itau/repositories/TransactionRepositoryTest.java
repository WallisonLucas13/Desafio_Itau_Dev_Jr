package com.example.desafio.itau.repositories;

import com.example.desafio.itau.models.TransactionModel;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @SuppressWarnings("unchecked")
    void testSave() throws NoSuchFieldException, IllegalAccessException {
        TransactionModel transactionMock = Instancio.create(TransactionModel.class);
        Field field = TransactionRepository.class.getDeclaredField("transactions");
        field.setAccessible(true);

        transactionRepository.save(transactionMock);

        List<TransactionModel> transactions = (List<TransactionModel>) field.get(transactionRepository);

        assertFalse(transactions.isEmpty());
        assertTrue(transactions.contains(transactionMock));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testFindAll() throws NoSuchFieldException, IllegalAccessException {
        TransactionModel transactionMock = Instancio.create(TransactionModel.class);
        TransactionModel transactionMock2 = Instancio.create(TransactionModel.class);

        Field field = TransactionRepository.class.getDeclaredField("transactions");
        field.setAccessible(true);
        List<TransactionModel> transactions = (List<TransactionModel>) field.get(transactionRepository);

        transactions.add(transactionMock);
        transactions.add(transactionMock2);

        List<TransactionModel> allTransactions = transactionRepository.findAll();

        assertTrue(allTransactions.contains(transactionMock));
        assertTrue(allTransactions.contains(transactionMock2));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDeleteAll() throws NoSuchFieldException, IllegalAccessException {
        TransactionModel transactionMock = Instancio.create(TransactionModel.class);
        TransactionModel transactionMock2 = Instancio.create(TransactionModel.class);

        Field field = TransactionRepository.class.getDeclaredField("transactions");
        field.setAccessible(true);
        List<TransactionModel> transactions = (List<TransactionModel>) field.get(transactionRepository);

        transactions.add(transactionMock);
        transactions.add(transactionMock2);

        assertTrue(transactions.contains(transactionMock));
        assertTrue(transactions.contains(transactionMock2));

        transactionRepository.deleteAll();

        assertTrue(transactions.isEmpty());
    }
}
