package com.Danielss44.seminovos.DTO.servicoVeiculo;

import com.Danielss44.seminovos.model.StatusServico;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ServicoVeiculoResponseDTO(
        Long id,

        String tipoServicoNome,

        String fornecedorNome,

        BigDecimal valor,

        LocalDate dataInicio,

        LocalDate dataConclusao,

        StatusServico status
) {
}
