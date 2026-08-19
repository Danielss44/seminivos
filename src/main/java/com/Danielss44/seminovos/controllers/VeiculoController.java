package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.veiculo.VeiculoRequestDTO;
import com.Danielss44.seminovos.DTO.veiculo.VeiculoResponseDTO;
import com.Danielss44.seminovos.model.StatusVeiculo;
import com.Danielss44.seminovos.services.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;

    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> cadastrar(@RequestBody @Valid VeiculoRequestDTO dto){
        VeiculoResponseDTO responseDTO = service.cadastrar(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorID(@PathVariable Long id){
        VeiculoResponseDTO responseDTO = service.buscarPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorPlaca(@PathVariable String placa){
        VeiculoResponseDTO responseDTO = service.buscarPorPlaca(placa);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/modelo")
    public ResponseEntity<List<VeiculoResponseDTO>> buscarPorModelo(@RequestParam String modelo){
        List<VeiculoResponseDTO> responseDTO = service.buscarPorModelo(modelo);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<VeiculoResponseDTO>> buscarPorStatus(@PathVariable StatusVeiculo status){
        List<VeiculoResponseDTO> responseDTO = service.buscarPorStatus(status);
        return ResponseEntity.ok(responseDTO);
    }



}
