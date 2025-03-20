package com.example.desafio.itau.controllers;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.models.StatisticModel;
import com.example.desafio.itau.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSaveTransaction() throws Exception {
        TransactionDto dto = new TransactionDto(new BigDecimal("10.0"), OffsetDateTime.now());
        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        Mockito.verify(transactionService, Mockito.times(1)).saveTransaction(Mockito.any());
    }

    @Test
    void testDeleteTransactions() throws Exception {
        mockMvc.perform(delete("/transacao"))
                .andExpect(status().isOk());

        Mockito.verify(transactionService, Mockito.times(1)).deleteTransactions();
    }

    @Test
    void testGetStatisticWithDefaultSeconds() throws Exception {
        StatisticModel statisticMock = Instancio.create(StatisticModel.class);
        Mockito.when(transactionService.getStatistic(60)).thenReturn(statisticMock);

        String responseStr = mockMvc.perform(get("/estatistica"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        StatisticModel statisticResponse = objectMapper.readValue(responseStr, StatisticModel.class);

        Mockito.verify(transactionService, Mockito.times(1)).getStatistic(60);
        assertEquals(statisticMock, statisticResponse);
    }

    @Test
    void testGetStatisticWithDefinedSeconds() throws Exception {
        StatisticModel statisticMock = Instancio.create(StatisticModel.class);
        Mockito.when(transactionService.getStatistic(360)).thenReturn(statisticMock);

        String responseStr = mockMvc.perform(get("/estatistica").param("intervalo", "360"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        StatisticModel statisticResponse = objectMapper.readValue(responseStr, StatisticModel.class);

        Mockito.verify(transactionService, Mockito.times(1)).getStatistic(360);
        assertEquals(statisticMock, statisticResponse);
    }
}
