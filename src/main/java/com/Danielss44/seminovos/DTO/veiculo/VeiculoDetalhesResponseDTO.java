package com.Danielss44.seminovos.DTO.veiculo;

import com.Danielss44.seminovos.DTO.h3.H3ResponseDTO;
import com.Danielss44.seminovos.DTO.revisao.RevisaoResponseDTO;
import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoResponseDTO;
import com.Danielss44.seminovos.model.StatusVeiculo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VeiculoDetalhesResponseDTO(

        Long id,

        String placa,

        String modelo,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
        Integer ano,

        String cor,

        int quilometragem,

        BigDecimal valor,

        LocalDate dataEntrada,

        StatusVeiculo status,

        RevisaoResponseDTO revisao,

        H3ResponseDTO h3,

        List<ServicoVeiculoResponseDTO> servicos,

        boolean prontoParaVenda
) {}
