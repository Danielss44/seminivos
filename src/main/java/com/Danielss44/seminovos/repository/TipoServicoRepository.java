package com.Danielss44.seminovos.repository;

import com.Danielss44.seminovos.model.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoServicoRepository extends JpaRepository<TipoServico, Long> {

    boolean existsByTipo(String tipo);
}
