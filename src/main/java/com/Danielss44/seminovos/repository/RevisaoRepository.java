package com.Danielss44.seminovos.repository;

import com.Danielss44.seminovos.model.Revisao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevisaoRepository extends JpaRepository<Revisao, Long> {
    boolean existsByVeiculoId(Long veiculoId);

    Optional<Revisao> findByVeiculoId(Long veiculoId);

    Optional<Revisao> findByVeiculoPlaca(String placa);

}
