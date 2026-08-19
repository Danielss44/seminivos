package com.Danielss44.seminovos.DTO.dashboard;

import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoResponseDTO;
import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoResponseDTO;
import com.Danielss44.seminovos.DTO.veiculo.VeiculoResponseDTO;
import com.Danielss44.seminovos.model.StatusVeiculo;

import java.util.List;
import java.util.Map;

public record DashboardResponseDTO (
        long totalVeiculos,
        Map<StatusVeiculo, Long> veiculoPorStatus,
        List<VeiculoResponseDTO> veiculosProntosParaVenda,
        List<VeiculoResponseDTO> veiculosEmPreparacao,
        List<ServicoVeiculoResponseDTO> servicosEmAndamento
){}
