package com.Danielss44.seminovos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placa;

    private String modelo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    private Integer ano;

    private String cor;

    private int quilometragem;

    private BigDecimal valor;

    private LocalDate dataEntrada;

    @Enumerated(EnumType.STRING)
    private StatusVeiculo status;

    @OneToOne(mappedBy = "veiculo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Revisao revisao;

    @OneToOne(mappedBy = "veiculo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private H3 h3;

    @OneToMany(mappedBy = "veiculo", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ServicoVeiculo> servicos;


    public Veiculo(@NotBlank String placa, @NotBlank String modelo, @NotNull Integer ano, @NotBlank String cor, @NotNull int quilometragem, @NotNull @Positive BigDecimal valor, @NotNull LocalDate localDate) {
    }
}
