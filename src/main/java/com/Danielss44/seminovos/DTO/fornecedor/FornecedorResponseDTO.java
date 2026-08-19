package com.Danielss44.seminovos.DTO.fornecedor;

import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoResponseDTO;

import java.util.List;

public record FornecedorResponseDTO (
    Long id,

    String nome,
    List<TipoServicoResponseDTO> tipoServicos
)
{}