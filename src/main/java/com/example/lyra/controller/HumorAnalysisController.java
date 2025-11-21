package com.example.lyra.controller;

import com.example.lyra.dto.request.HumorAnalysisRequest;
import com.example.lyra.dto.response.HumorAnalysisResponse;
import com.example.lyra.service.HumorAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/humor")
@RequiredArgsConstructor
public class HumorAnalysisController {
    
    private final HumorAnalysisService humorAnalysisService;
    
    // Endpoint para análise de humor com IA
    @PostMapping("/analisar")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<HumorAnalysisResponse> analisarHumor(
            @Valid @RequestBody HumorAnalysisRequest request) {
        
        HumorAnalysisResponse response = humorAnalysisService.processarAnaliseHumor(request);
        return ResponseEntity.ok(response);
    }
}
