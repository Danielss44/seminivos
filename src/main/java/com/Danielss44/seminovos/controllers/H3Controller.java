package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.h3.H3RequestDTO;
import com.Danielss44.seminovos.DTO.h3.H3ResponseDTO;
import com.Danielss44.seminovos.services.H3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/h3")
@RequiredArgsConstructor
public class H3Controller {

    private final H3Service service;

    @PostMapping
    public ResponseEntity<H3ResponseDTO> criar(@RequestBody @Valid H3RequestDTO dto){
        H3ResponseDTO responseDTO = service.criar(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @PatchMapping("/finalizar/{placa}")
    public ResponseEntity<Void> finalizar(@PathVariable String placa){
        service.finalizar(placa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<H3ResponseDTO> buscarPorVeiculo(@PathVariable String placa){
        H3ResponseDTO responseDTO = service.buscarPorVeiculo(placa);
        return ResponseEntity.ok(responseDTO);
    }

}
