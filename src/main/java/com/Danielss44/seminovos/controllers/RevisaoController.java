package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.revisao.RevisaoRequestDTO;
import com.Danielss44.seminovos.DTO.revisao.RevisaoResponseDTO;
import com.Danielss44.seminovos.services.RevisaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/revisoes")
@RequiredArgsConstructor
public class RevisaoController {

    private final RevisaoService service;

    @PostMapping
    public ResponseEntity<RevisaoResponseDTO> criar(@RequestBody @Valid RevisaoRequestDTO dto){
        RevisaoResponseDTO responseDTO = service.criar(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @PatchMapping("/finalizar/{placa}")
    public ResponseEntity<Void> finalizar(@PathVariable String placa){
        service.finalizar(placa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{placa}")
    ResponseEntity<RevisaoResponseDTO> buscarPorVeiculo(@PathVariable String placa){
        RevisaoResponseDTO responseDTO = service.buscarPorVeiculo(placa);
        return ResponseEntity.ok(responseDTO);
    }
}
