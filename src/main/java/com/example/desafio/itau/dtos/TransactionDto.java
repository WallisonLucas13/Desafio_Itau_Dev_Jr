package com.example.desafio.itau.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionDto(@JsonProperty(value = "valor") BigDecimal value, @JsonProperty(value = "dataHora") OffsetDateTime dateHour) {
}
