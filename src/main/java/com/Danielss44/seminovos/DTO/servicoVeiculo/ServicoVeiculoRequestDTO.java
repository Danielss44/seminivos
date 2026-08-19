package com.Danielss44.seminovos.DTO.servicoVeiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ServicoVeiculoRequestDTO(

        @NotNull
        Long tipoServicoId,

        @NotNull
        Long fornecedorId,

        @NotNull
        @Positive
        BigDecimal valor,

        @NotNull
        LocalDate dataInicio,


        @NotNull
        Long veiculoId

) {
}
