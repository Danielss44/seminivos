package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoRequestDTO;
import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoResponseDTO;
import com.Danielss44.seminovos.exception.EntidadeNaoEncontradaException;
import com.Danielss44.seminovos.exception.RegraDeNegocioException;
import com.Danielss44.seminovos.model.*;
import com.Danielss44.seminovos.repository.FornecedorRepository;
import com.Danielss44.seminovos.repository.ServicoVeiculoRepository;
import com.Danielss44.seminovos.repository.TipoServicoRepository;
import com.Danielss44.seminovos.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ServicoVeiculoService {
    private final ServicoVeiculoRepository repository;

    private final TipoServicoRepository tipoServicoRepository;

    private final FornecedorRepository fornecedorRepository;

    private final VeiculoRepository veiculoRepository;

    private final VeiculoService veiculoService;

    private ServicoVeiculoResponseDTO toResponseDTO(ServicoVeiculo servicoVeiculo){
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

    @Transactional
    public ServicoVeiculoResponseDTO criar(ServicoVeiculoRequestDTO dto){
        if(repository.existsByVeiculoIdAndTipoServicoIdAndStatusNot(dto.veiculoId(), dto.tipoServicoId(),StatusServico.CONCLUIDO)){
            throw new RegraDeNegocioException("Este tipo de serviço já está em andamento para este veículo");
        }


        TipoServico tipoServico = tipoServicoRepository.findById(dto.tipoServicoId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo de serviço não encontrado"));

        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado"));
        boolean fornecedorExecutaTipo = fornecedor.getTipoServico()
                .stream().anyMatch(tipo -> tipo.getId().equals(dto.tipoServicoId()));

        if (!fornecedorExecutaTipo){
            throw new RegraDeNegocioException("Este fornecedor não executa este tipo de serviço");
        }

        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));

        ServicoVeiculo servicoVeiculo = ServicoVeiculo.builder()
                .tipoServico(tipoServico)
                .fornecedor(fornecedor)
                .valor(dto.valor())
                .dataInicio(dto.dataInicio())
                .veiculo(veiculo)
                .status(StatusServico.EM_ANDAMENTO)
                .build();

        repository.save(servicoVeiculo);

        return toResponseDTO(servicoVeiculo);
    }

    @Transactional
    public void concluir(Long id) {
        ServicoVeiculo servicoVeiculo = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado para esse veículo"));
        servicoVeiculo.setDataConclusao(LocalDate.now());
        servicoVeiculo.setStatus(StatusServico.CONCLUIDO);
        repository.save(servicoVeiculo);
        veiculoService.verificarEAtualizarStatus(servicoVeiculo.getVeiculo());
    }

    @Transactional(readOnly = true)
    public List<ServicoVeiculoResponseDTO> listarPorVeiculo(String placa){
        List<ServicoVeiculo> servicoVeiculo = repository.findByVeiculoPlaca(placa);
        return servicoVeiculo
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
