package com.Danielss44.seminovos.DTO.h3;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record H3RequestDTO(

        @NotNull
        Long fornecedorId,

        boolean finalizado,

        LocalDate dataH3,

        @NotNull
        Long veiculoId
) {
}
