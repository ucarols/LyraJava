package com.example.lyra.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiAnalysisResponse {
    
    @JsonProperty("ResumoRecebido")
    private String resumoRecebido;
    
    @JsonProperty("Nivel")
    private Integer nivel;
}
