package com.Danielss44.seminovos.controllers;

import com.Danielss44.seminovos.DTO.dashboard.DashboardResponseDTO;
import com.Danielss44.seminovos.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> gerarDashboard(){
        DashboardResponseDTO dashboard = service.gerarDashboard();
        return ResponseEntity.ok(dashboard);
    }
}
