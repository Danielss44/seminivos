package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.revisao.RevisaoRequestDTO;
import com.Danielss44.seminovos.DTO.revisao.RevisaoResponseDTO;
import com.Danielss44.seminovos.exception.EntidadeNaoEncontradaException;
import com.Danielss44.seminovos.exception.RegraDeNegocioException;
import com.Danielss44.seminovos.model.Revisao;
import com.Danielss44.seminovos.model.Veiculo;
import com.Danielss44.seminovos.repository.RevisaoRepository;
import com.Danielss44.seminovos.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RevisaoService {

    private final RevisaoRepository repository;

    private final VeiculoRepository veiculoRepository;

    private final VeiculoService veiculoService;

    private RevisaoResponseDTO toResponseDTO(Revisao revisao){
        return new RevisaoResponseDTO(
                revisao.getId(),
                revisao.isFinalizado(),
                revisao.getDataRevisao(),
                revisao.getObservacoes()
        );
    }

    @Transactional
    public RevisaoResponseDTO criar(RevisaoRequestDTO dto){
        if(repository.existsByVeiculoId(dto.veiculoId())){
            throw new RegraDeNegocioException("Revisao ja lançada");
        }

        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));


        Revisao revisao = Revisao.builder()
                .finalizado(dto.finalizado())
                .dataRevisao(dto.dataRevisao())
                .observacoes(dto.observacoes())
                .veiculo(veiculo)
                .build();
        repository.save(revisao);

        return toResponseDTO(revisao);

    }
    @Transactional
    public void finalizar(String placa){
        Revisao revisao = repository.findByVeiculoPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Revisão não encontrada para esse veículo"));
        revisao.setFinalizado(true);
        repository.save(revisao);
        veiculoService.verificarEAtualizarStatus(revisao.getVeiculo());
    }
    @Transactional(readOnly = true)
    public RevisaoResponseDTO buscarPorVeiculo(String placa){
        Revisao revisao = repository.findByVeiculoPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));
        return toResponseDTO(revisao);
    }

}
