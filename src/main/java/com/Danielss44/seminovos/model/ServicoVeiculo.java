package com.Danielss44.seminovos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicoVeiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipoServico_id")
    private TipoServico tipoServico;

    @ManyToOne
    @JoinColumn(name = "fornecedor")
    private  Fornecedor fornecedor;

    private BigDecimal valor;

    private LocalDate dataInicio;

    private LocalDate dataConclusao;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    private StatusServico status;
}
