package com.Danielss44.seminovos.DTO.veiculo;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;


public record VeiculoRequestDTO(

        @NotBlank
        String placa,

        @NotBlank
        String modelo,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
        Integer ano,

        @NotBlank
        String cor,

        @NotNull
        int quilometragem,

        @NotNull
        @Positive
        BigDecimal valor,

        @NotNull
        LocalDate dataEntrada
) {}
