package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoRequestDTO;
import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoResponseDTO;
import com.Danielss44.seminovos.exception.EntidadeNaoEncontradaException;
import com.Danielss44.seminovos.exception.RegraDeNegocioException;
import com.Danielss44.seminovos.model.TipoServico;
import com.Danielss44.seminovos.repository.TipoServicoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TipoServicoService {

    private final TipoServicoRepository repository;

    private TipoServicoResponseDTO toResponseDTO(TipoServico tipoServico){
        return new TipoServicoResponseDTO(
                tipoServico.getId(),
                tipoServico.getTipo()
        );
    }

    @Transactional
    public TipoServicoResponseDTO cadastrar(TipoServicoRequestDTO dto){
        if(repository.existsByTipo(dto.tipo())){
            throw new RegraDeNegocioException("Tipo de serviço já cadastrado");
        }

        TipoServico tipoServico = TipoServico.builder()
                .tipo(dto.tipo())
                .build();

        repository.save(tipoServico);
        return toResponseDTO(tipoServico);
    }

    @Transactional(readOnly = true)
    public List<TipoServicoResponseDTO> listar(){
        return repository.findAll().stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public TipoServicoResponseDTO buscarPorId(Long id){
        TipoServico tipoServico = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo de serviço não encontrado"));
        return toResponseDTO(tipoServico);
    }
}
