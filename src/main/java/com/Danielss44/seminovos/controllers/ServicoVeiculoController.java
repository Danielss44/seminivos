package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoRequestDTO;
import com.Danielss44.seminovos.DTO.servicoVeiculo.ServicoVeiculoResponseDTO;
import com.Danielss44.seminovos.services.ServicoVeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoVeiculoController {

    private final ServicoVeiculoService service;

    @PostMapping
    public ResponseEntity<ServicoVeiculoResponseDTO> criar(@RequestBody @Valid ServicoVeiculoRequestDTO dto){
        ServicoVeiculoResponseDTO responseDTO = service.criar(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @PatchMapping("/concluir/{id}")
    public ResponseEntity<Void> concluir(@PathVariable Long id){
        service.concluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{placa}")
    public ResponseEntity<List<ServicoVeiculoResponseDTO>> listarPorVeiculo(@PathVariable String placa){
        List<ServicoVeiculoResponseDTO> responseDTOS = service.listarPorVeiculo(placa);
        return ResponseEntity.ok(responseDTOS);
    }
}
