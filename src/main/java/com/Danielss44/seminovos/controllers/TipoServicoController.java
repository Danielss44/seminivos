package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoRequestDTO;
import com.Danielss44.seminovos.DTO.tipoServico.TipoServicoResponseDTO;
import com.Danielss44.seminovos.services.TipoServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-servico")
@RequiredArgsConstructor
public class TipoServicoController {

    private final TipoServicoService service;

    @PostMapping
    public ResponseEntity<TipoServicoResponseDTO> cadastrar(@RequestBody @Valid TipoServicoRequestDTO dto){
        TipoServicoResponseDTO responseDTO = service.cadastrar(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<TipoServicoResponseDTO>> listar(){
        List<TipoServicoResponseDTO> responseDTOS = service.listar();
        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicoResponseDTO> buscarPorId(@PathVariable Long id){
        TipoServicoResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

}
