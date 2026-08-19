package com.Danielss44.seminovos.DTO.revisao;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RevisaoRequestDTO(

        boolean finalizado,

        @NotNull
        LocalDate dataRevisao,

        String observacoes,

        @NotNull
        Long veiculoId

) {
}
