package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.fornecedor.FornecedorRequestDTO;
import com.Danielss44.seminovos.DTO.fornecedor.FornecedorResponseDTO;
import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoResponseDTO;
import com.Danielss44.seminovos.exception.EntidadeNaoEncontradaException;
import com.Danielss44.seminovos.exception.RegraDeNegocioException;
import com.Danielss44.seminovos.model.Fornecedor;
import com.Danielss44.seminovos.model.TipoServico;
import com.Danielss44.seminovos.repository.FornecedorRepository;
import com.Danielss44.seminovos.repository.TipoServicoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FornecedorService {

    private final FornecedorRepository repository;

    private final TipoServicoRepository tipoServicoRepository;

    private FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor){
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getTipoServico()
                        .stream()
                        .map(tipo -> new TipoServicoResponseDTO(tipo.getId(), tipo.getTipo()))
                        .toList()
        );
    }

    @Transactional
    public FornecedorResponseDTO cadastrar(FornecedorRequestDTO dto){
        if(repository.existsByNome(dto.nome())){
            throw new RegraDeNegocioException("Fornecedor ja cadastrado");
        }
        List<TipoServico> tipos = tipoServicoRepository.findAllById(dto.tipoServicoIds());

        Fornecedor fornecedor = Fornecedor.builder()
                .nome(dto.nome())
                .tipoServico(tipos)
                .build();

        repository.save(fornecedor);

        return toResponseDTO(fornecedor);
    }

    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarPorId(Long id){
        Fornecedor fornecedor =repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado"));
        return toResponseDTO(fornecedor);
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> listarPorTipoServico(Long tipoServicoId){
        List<FornecedorResponseDTO> fornecedores = repository.findByTipoServicoId(tipoServicoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return fornecedores;
    }

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> listar(){
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }
}
