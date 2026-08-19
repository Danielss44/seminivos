package com.Danielss44.seminovos.DTO.fornecedor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FornecedorRequestDTO(
        @NotBlank
        String nome,

        @NotEmpty
        List<Long> tipoServicoIds
)
{}
