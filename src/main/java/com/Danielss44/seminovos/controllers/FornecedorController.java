package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.fornecedor.FornecedorRequestDTO;
import com.Danielss44.seminovos.DTO.fornecedor.FornecedorResponseDTO;
import com.Danielss44.seminovos.services.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService service;

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> cadastrar(@RequestBody @Valid FornecedorRequestDTO dto){
        FornecedorResponseDTO responseDTO = service.cadastrar(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(@PathVariable Long id){
        FornecedorResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/tipo/{tipoServico}")
    public ResponseEntity<List<FornecedorResponseDTO>> listarPorTipoServico(@PathVariable Long tipoServico) {
        List<FornecedorResponseDTO> responseDTOS = service.listarPorTipoServico(tipoServico);
        return ResponseEntity.ok(responseDTOS);

    }

    @GetMapping()
    public ResponseEntity<List<FornecedorResponseDTO>> listar() {
        List<FornecedorResponseDTO> responseDTOS = service.listar();
        return ResponseEntity.ok(responseDTOS);

    }
}
