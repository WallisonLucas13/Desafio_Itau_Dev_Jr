package com.example.desafio.itau.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionDto(
        @JsonProperty(value = "valor", required = true) BigDecimal value,
        @JsonProperty(value = "dataHora", required = true) OffsetDateTime dateHour
) {
}
