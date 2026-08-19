package com.Danielss44.seminovos.repository;

import com.Danielss44.seminovos.model.ServicoVeiculo;
import com.Danielss44.seminovos.model.StatusServico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicoVeiculoRepository  extends JpaRepository<ServicoVeiculo, Long> {

    boolean existsByVeiculoIdAndStatusNot(Long veiculoId, StatusServico status);

    List<ServicoVeiculo> findByVeiculoPlaca(String Placa);

    boolean existsByVeiculoIdAndTipoServicoIdAndStatusNot(Long veiculoId, Long tipoServicoId, StatusServico status);

    List<ServicoVeiculo> findByStatus(StatusServico status);
}
