package com.Danielss44.seminovos.DTO.revisao;

import java.time.LocalDate;

public record RevisaoResponseDTO(

        Long id,

        boolean finalizado,

        LocalDate dataRevisao,

        String observacoes
) {}
