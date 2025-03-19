package com.example.desafio.itau.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticModel {
    private long count = 0;
    private double sum = 0.0;
    private double avg = 0.0;
    private double min = 0.0;
    private double max = 0.0;
}
