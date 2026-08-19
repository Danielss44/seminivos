package com.Danielss44.seminovos.DTO.veiculo;

import com.Danielss44.seminovos.model.StatusVeiculo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VeiculoResponseDTO(

        Long id,

        String placa,

        String modelo,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
        Integer ano,

        String cor,

        int quilometragem,

        BigDecimal valor,

        LocalDate dataEntrada,

        StatusVeiculo status
) {
}
