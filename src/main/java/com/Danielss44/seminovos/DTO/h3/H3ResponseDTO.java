package com.Danielss44.seminovos.DTO.h3;

import java.time.LocalDate;

public record H3ResponseDTO(
        Long id,

        String fornecedorNome,

        boolean finalizado,

        LocalDate dataH3
) {
}
