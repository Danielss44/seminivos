package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.h3.H3RequestDTO;
import com.Danielss44.seminovos.DTO.h3.H3ResponseDTO;
import com.Danielss44.seminovos.exception.EntidadeNaoEncontradaException;
import com.Danielss44.seminovos.exception.RegraDeNegocioException;
import com.Danielss44.seminovos.model.Fornecedor;
import com.Danielss44.seminovos.model.H3;
import com.Danielss44.seminovos.model.Veiculo;
import com.Danielss44.seminovos.repository.FornecedorRepository;
import com.Danielss44.seminovos.repository.H3Repository;
import com.Danielss44.seminovos.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class H3Service {

    private final H3Repository repository;

    private final VeiculoRepository veiculoRepository;

    private final VeiculoService veiculoService;

    private final FornecedorRepository fornecedorRepository;

    private H3ResponseDTO toResponseDTO(H3 h3){
        return new H3ResponseDTO(
                h3.getId(),
                h3.getFornecedor().getNome(),
                h3.isFinalizado(),
                h3.getDataH3()
        );
    }

    @Transactional
    public H3ResponseDTO criar(H3RequestDTO dto){

        if(repository.existsByVeiculoId(dto.veiculoId())){
            throw new RegraDeNegocioException("H3 já lançada");
        }

        Veiculo veiculo = veiculoRepository.findById(dto.veiculoId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));

        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encrontado"));
        H3 h3 = H3.builder()
                .fornecedor(fornecedor)
                .finalizado(dto.finalizado())
                .dataH3(dto.dataH3())
                .veiculo(veiculo)
                .build();

        repository.save(h3);
        return toResponseDTO(h3);
    }

    @Transactional
    public void finalizar(String placa){
        H3 h3 = repository.findByVeiculoPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("H3 não encontrada nesse veículo"));
        h3.setFinalizado(true);
        repository.save(h3);
        veiculoService.verificarEAtualizarStatus(h3.getVeiculo());
    }

    @Transactional(readOnly = true)
    public H3ResponseDTO buscarPorVeiculo(String placa){
        H3 h3 = repository.findByVeiculoPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));
        return toResponseDTO(h3);
    }


}
