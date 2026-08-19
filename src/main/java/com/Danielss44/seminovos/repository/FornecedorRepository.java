package com.Danielss44.seminovos.repository;

import com.Danielss44.seminovos.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByTipoServicoId(Long id);

    boolean existsByNome(String nome);
}
