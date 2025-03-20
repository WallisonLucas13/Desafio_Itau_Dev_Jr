package com.example.desafio.itau.services;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.exceptions.FutureDateException;
import com.example.desafio.itau.exceptions.NegativeValueException;
import com.example.desafio.itau.models.StatisticModel;
import com.example.desafio.itau.models.TransactionModel;
import com.example.desafio.itau.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    private final long DEFAULT_SECONDS = 60;

    @Test
    void testSaveTransaction() throws InterruptedException {
        TransactionDto dto = new TransactionDto(new BigDecimal("10.0"), OffsetDateTime.now());

        transactionService.saveTransaction(dto);

        Thread.sleep(500);

        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testDeleteTransactions() {
        transactionService.deleteTransactions();

        Mockito.verify(transactionRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void testGetStatisticWhenEmptyTransactionsList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = TransactionService.class.getDeclaredMethod("getTransactionsWithLimitSeconds", long.class);
        method.setAccessible(true);

        Mockito.when(method.invoke(transactionService, DEFAULT_SECONDS)).thenReturn(List.of());

        StatisticModel statistic = transactionService.getStatistic(DEFAULT_SECONDS);

        assertEquals(0, statistic.getCount());
        assertEquals(0, statistic.getSum());
        assertEquals(0, statistic.getAvg());
        assertEquals(0, statistic.getMax());
        assertEquals(0, statistic.getMin());
    }

    @Test
    void testGetStatisticWhenHasTransactionOnList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<TransactionModel> transactionsMock = List.of(
                new TransactionModel(UUID.randomUUID(), new BigDecimal("10.0"), OffsetDateTime.now()),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("20.0"), OffsetDateTime.now()),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("30.0"), OffsetDateTime.now()),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("40.0"), OffsetDateTime.now())
        );

        Method method = TransactionService.class.getDeclaredMethod("getTransactionsWithLimitSeconds", long.class);
        method.setAccessible(true);
        Mockito.when(method.invoke(transactionService, DEFAULT_SECONDS)).thenReturn(transactionsMock);

        StatisticModel statistic = transactionService.getStatistic(DEFAULT_SECONDS);

        DoubleSummaryStatistics expectedStatistics = transactionsMock.parallelStream()
                .mapToDouble(transaction -> transaction.getValue().doubleValue())
                .summaryStatistics();

        assertEquals(expectedStatistics.getCount(), statistic.getCount());
        assertEquals(expectedStatistics.getSum(), statistic.getSum());
        assertEquals(expectedStatistics.getAverage(), statistic.getAvg());
        assertEquals(expectedStatistics.getMax(), statistic.getMax());
        assertEquals(expectedStatistics.getMin(), statistic.getMin());
    }

    @Test
    void testValidateTransactionDtoWhenTransactionIsValid() throws NoSuchMethodException{
        TransactionDto dto = new TransactionDto(new BigDecimal("10.0"), OffsetDateTime.now());
        Method method = TransactionService.class.getDeclaredMethod("validateTransactionDto", TransactionDto.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(transactionService, dto));
    }

    @Test
    void testValidateTransactionDtoWhenValueIsNegative() throws NoSuchMethodException {
        TransactionDto dto = new TransactionDto(new BigDecimal("-10.0"), OffsetDateTime.now());
        Method method = TransactionService.class.getDeclaredMethod("validateTransactionDto", TransactionDto.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(transactionService, dto));
        assertTrue(exception.getCause() instanceof NegativeValueException);
    }

    @Test
    void testValidateTransactionDtoWhenDateHourToFuture() throws NoSuchMethodException {
        TransactionDto dto = new TransactionDto(new BigDecimal("10.0"), OffsetDateTime.now().plusSeconds(DEFAULT_SECONDS));
        Method method = TransactionService.class.getDeclaredMethod("validateTransactionDto", TransactionDto.class);
        method.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(transactionService, dto));
        assertTrue(exception.getCause() instanceof FutureDateException);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetTransactionsWithLimitSecondsWhenAllTransactionsWithinLimit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = TransactionService.class.getDeclaredMethod("getTransactionsWithLimitSeconds", long.class);
        method.setAccessible(true);

        List<TransactionModel> transactionsMock = List.of(
                new TransactionModel(UUID.randomUUID(), new BigDecimal("10.0"), OffsetDateTime.now()),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("20.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS - 1)),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("30.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS - 10)),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("40.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS - 20))
        );

        Mockito.when(transactionRepository.findAll()).thenReturn(transactionsMock);

        List<TransactionModel> transactions = (List<TransactionModel>) method.invoke(transactionService, DEFAULT_SECONDS);

        assertEquals(transactionsMock.size(), transactions.size());
        assertTrue(transactions.containsAll(transactionsMock));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetTransactionsWithLimitSecondsWhenNotAllTransactionsWithinLimit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = TransactionService.class.getDeclaredMethod("getTransactionsWithLimitSeconds", long.class);
        method.setAccessible(true);

        List<TransactionModel> transactionsMock = List.of(
                new TransactionModel(UUID.randomUUID(), new BigDecimal("10.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS + 1)),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("20.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS + 10)),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("30.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS + 20)),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("40.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS + 30))
        );

        Mockito.when(transactionRepository.findAll()).thenReturn(transactionsMock);

        List<TransactionModel> transactions = (List<TransactionModel>) method.invoke(transactionService, DEFAULT_SECONDS);

        assertEquals(0, transactions.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetTransactionsWithLimitSecondsWhenSomeTransactionsWithinLimit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = TransactionService.class.getDeclaredMethod("getTransactionsWithLimitSeconds", long.class);
        method.setAccessible(true);

        List<TransactionModel> transactionsMock = List.of(
                new TransactionModel(UUID.randomUUID(), new BigDecimal("10.0"), OffsetDateTime.now()),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("20.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS + 1)),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("30.0"), OffsetDateTime.now()),
                new TransactionModel(UUID.randomUUID(), new BigDecimal("40.0"), OffsetDateTime.now().minusSeconds(DEFAULT_SECONDS + 1))
        );

        Mockito.when(transactionRepository.findAll()).thenReturn(transactionsMock);

        List<TransactionModel> transactions = (List<TransactionModel>) method.invoke(transactionService, DEFAULT_SECONDS);

        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transactionsMock.get(0)));
        assertFalse(transactions.contains(transactionsMock.get(1)));
        assertTrue(transactions.contains(transactionsMock.get(2)));
        assertFalse(transactions.contains(transactionsMock.get(3)));
    }
}
