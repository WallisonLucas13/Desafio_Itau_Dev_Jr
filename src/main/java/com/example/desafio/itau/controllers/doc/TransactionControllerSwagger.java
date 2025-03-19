package com.example.desafio.itau.controllers.doc;

import com.example.desafio.itau.dtos.TransactionDto;
import com.example.desafio.itau.models.StatisticModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Transação")
public interface TransactionControllerSwagger {
    @Operation(summary = "Salvar uma transação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "Json inválido"),
            @ApiResponse(responseCode = "422", description = "Valor negativo ou data futura")
    })
    ResponseEntity<String> saveTransaction(
            @RequestBody(
                    description = "Transação a ser salva",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TransactionDto.class))
            ) TransactionDto dto
    );

    @Operation(summary = "Deletar todas as transações")
    @ApiResponse(
            responseCode = "200",
            description = "Todas as transações deletadas com sucesso"
    )
    ResponseEntity<String> deleteTransactions();

    @Operation(summary = "Obter estatísticas")
    @ApiResponse(
            responseCode = "200",
            description = "Estatísticas recuperadas",
            content = @Content(schema = @Schema(implementation = StatisticModel.class))
    )
    ResponseEntity<StatisticModel> getStatistic(int seconds);
}
