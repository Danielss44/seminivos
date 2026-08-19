package com.Danielss44.seminovos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class H3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    private boolean finalizado;

    private LocalDate dataH3;

    @OneToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;
}
