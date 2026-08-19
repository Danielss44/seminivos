package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.dashboard.DashboardResponseDTO;
import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoResponseDTO;
import com.Danielss44.seminovos.DTO.veiculo.VeiculoResponseDTO;
import com.Danielss44.seminovos.model.ServicoVeiculo;
import com.Danielss44.seminovos.model.StatusServico;
import com.Danielss44.seminovos.model.StatusVeiculo;
import com.Danielss44.seminovos.model.Veiculo;
import com.Danielss44.seminovos.repository.ServicoVeiculoRepository;
import com.Danielss44.seminovos.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VeiculoRepository veiculoRepository;

    private final ServicoVeiculoRepository servicoVeiculoRepository;

    private VeiculoResponseDTO toVeiculoDTO(Veiculo veiculo){
        return new VeiculoResponseDTO(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCor(),
                veiculo.getQuilometragem(),
                veiculo.getValor(),
                veiculo.getDataEntrada(),
                veiculo.getStatus()
        );
    }

    private ServicoVeiculoResponseDTO toServicoDTO(ServicoVeiculo servicoVeiculo){
        return new ServicoVeiculoResponseDTO(
                servicoVeiculo.getId(),
                servicoVeiculo.getTipoServico().getTipo(),
                servicoVeiculo.getFornecedor().getNome(),
                servicoVeiculo.getValor(),
                servicoVeiculo.getDataInicio(),
                servicoVeiculo.getDataConclusao(),
                servicoVeiculo.getStatus()
        );
    }


    @Transactional(readOnly = true)
    public DashboardResponseDTO gerarDashboard(){



        long totalVeiculos = veiculoRepository.count();

        Map<StatusVeiculo, Long> veiculosPorStatus =  new EnumMap<>(StatusVeiculo.class);
        for (StatusVeiculo statusVeiculo  : StatusVeiculo.values()){
            veiculosPorStatus.put(statusVeiculo, veiculoRepository.countByStatus(statusVeiculo));
        }

        List<VeiculoResponseDTO> prontos = veiculoRepository
                .findByStatus(StatusVeiculo.PRONTO_PARA_VENDA)
                .stream()
                .map(this::toVeiculoDTO)
                .toList();

        List<VeiculoResponseDTO> emPreparacao = veiculoRepository
                .findByStatus(StatusVeiculo.EM_PREPARACAO)
                .stream()
                .map(this::toVeiculoDTO)
                .toList();

        List<ServicoVeiculoResponseDTO> servicos = servicoVeiculoRepository
                .findByStatus(StatusServico.EM_ANDAMENTO)
                .stream()
                .map(this::toServicoDTO)
                .toList();

        return new DashboardResponseDTO(
                totalVeiculos,
                veiculosPorStatus,
                prontos,
                emPreparacao,
                servicos
        );
    }
}
