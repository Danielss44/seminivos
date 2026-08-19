package com.Danielss44.seminovos.repository;

import com.Danielss44.seminovos.model.StatusVeiculo;
import com.Danielss44.seminovos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Optional<Veiculo> findByPlaca(String placa);

    List<Veiculo> findByModeloContainingIgnoreCase(String modelo);

    List<Veiculo> findByStatus(StatusVeiculo statusVeiculo);

    boolean existsByPlaca(String placa);

    long countByStatus(StatusVeiculo status);

}
