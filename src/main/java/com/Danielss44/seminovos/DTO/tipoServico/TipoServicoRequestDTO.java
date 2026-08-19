package com.Danielss44.seminovos.DTO.tipoServico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoServicoRequestDTO(
        @NotBlank
        String tipo
)
{}
