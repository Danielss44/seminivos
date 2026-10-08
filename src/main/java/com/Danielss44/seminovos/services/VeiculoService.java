package com.Danielss44.seminovos.services;

import com.Danielss44.seminovos.DTO.h3.H3ResponseDTO;
import com.Danielss44.seminovos.DTO.revisao.RevisaoResponseDTO;
import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoResponseDTO;
import com.Danielss44.seminovos.DTO.veiculo.VeiculoDetalhesResponseDTO;
import com.Danielss44.seminovos.DTO.veiculo.VeiculoRequestDTO;
import com.Danielss44.seminovos.DTO.veiculo.VeiculoResponseDTO;
import com.Danielss44.seminovos.exception.EntidadeNaoEncontradaException;
import com.Danielss44.seminovos.exception.RegraDeNegocioException;
import com.Danielss44.seminovos.model.*;
import com.Danielss44.seminovos.repository.H3Repository;
import com.Danielss44.seminovos.repository.RevisaoRepository;
import com.Danielss44.seminovos.repository.ServicoVeiculoRepository;
import com.Danielss44.seminovos.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    private final VeiculoRepository repository;

    private final RevisaoRepository revisaoRepository;

    private final H3Repository h3Repository;

    private final ServicoVeiculoRepository servicoVeiculoRepository;

    private VeiculoResponseDTO toResponseDTO(Veiculo veiculo){
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

    @Transactional
    public VeiculoResponseDTO cadastrar(VeiculoRequestDTO dto){
        if(repository.existsByPlaca(dto.placa())){
            throw new RegraDeNegocioException("Veículo já cadastrado");
        }
        Veiculo veiculo =Veiculo.builder()
                .placa(dto.placa())
                .modelo(dto.modelo())
                .ano(dto.ano())
                .cor(dto.cor())
                .quilometragem(dto.quilometragem())
                .valor(dto.valor())
                .dataEntrada(dto.dataEntrada())
                .status(StatusVeiculo.EM_PREPARACAO)
                .build();

        repository.save(veiculo);

        return toResponseDTO(veiculo);

    }
    @Transactional(readOnly = true)
    public VeiculoResponseDTO buscarPorId(Long id){
        Veiculo veiculo = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));
        return toResponseDTO(veiculo);
    }
    @Transactional(readOnly = true)
    public VeiculoResponseDTO buscarPorPlaca(String placa){
        Veiculo veiculo = repository.findByPlaca(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));
        return toResponseDTO(veiculo);
    }
    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> buscarPorModelo(String modelo){
        return repository.findByModeloContainingIgnoreCase(modelo)
                .stream()
                .map(this::toResponseDTO )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> buscarPorStatus(StatusVeiculo status){
        return repository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void verificarEAtualizarStatus(Veiculo veiculo){
        //Verifica Revisao
        Optional<Revisao> revisao = revisaoRepository.findByVeiculoId(veiculo.getId());
        if (revisao.isEmpty() || !revisao.get().isFinalizado()) return;

        //Verifica H3
        Optional<H3> h3 = h3Repository.findByVeiculoId(veiculo.getId());
        if (h3.isEmpty() || !h3.get().isFinalizado()) return;


        //Verifica serviços
        if(servicoVeiculoRepository.existsByVeiculoIdAndStatusNot(veiculo.getId(), StatusServico.CONCLUIDO)) return;

        veiculo.setStatus(StatusVeiculo.PRONTO_PARA_VENDA);
        repository.save(veiculo);

    }

    @Transactional(readOnly = true)
    public VeiculoDetalhesResponseDTO buscarDetalhes(Long id){
        Veiculo veiculo = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));

        RevisaoResponseDTO revisaoDTO = revisaoRepository.findByVeiculoId(veiculo.getId())
                .map(r -> new RevisaoResponseDTO(r.getId(),r.isFinalizado(), r.getDataRevisao(), r.getObservacoes()))
                .orElse(null);

        H3ResponseDTO h3DTO = h3Repository.findByVeiculoId(veiculo.getId())
                .map(h -> new H3ResponseDTO(h.getId(), h.getFornecedor().getNome(), h.isFinalizado(), h.getDataH3()))
                .orElse(null);

        List<ServicoVeiculoResponseDTO> servicosDTO = servicoVeiculoRepository.findByVeiculoPlaca(veiculo.getPlaca())
                .stream()
                .map(s -> new ServicoVeiculoResponseDTO(
                        s.getId(),
                        s.getTipoServico().getTipo(),
                        s.getFornecedor().getNome(),
                        s.getValor(),
                        s.getDataInicio(),
                        s.getDataConclusao(),
                        s.getStatus()
                ))
                .toList();

        boolean prontoParaVenda = veiculo.getStatus() == StatusVeiculo.PRONTO_PARA_VENDA;

        return new VeiculoDetalhesResponseDTO(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getCor(),
                veiculo.getQuilometragem(),
                veiculo.getValor(),
                veiculo.getDataEntrada(),
                veiculo.getStatus(),
                revisaoDTO,
                h3DTO,
                servicosDTO,
                prontoParaVenda
        );
    }
}
