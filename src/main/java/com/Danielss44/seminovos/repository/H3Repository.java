package com.Danielss44.seminovos.repository;

import com.Danielss44.seminovos.model.H3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface H3Repository extends JpaRepository<H3, Long> {
    Optional<H3> findByVeiculoPlaca(String placa);

    boolean existsByVeiculoId(Long veiculoId);

    Optional<H3> findByVeiculoId(Long veiculoId);
}
