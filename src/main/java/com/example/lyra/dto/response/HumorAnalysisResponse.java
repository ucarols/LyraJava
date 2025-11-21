package com.example.lyra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumorAnalysisResponse {
    
    private String resumo;
    private Integer nivelGravidade;
    private String mensagem;
    private String orientacao;
    private Boolean requerAtencaoImediata;
    private String fonte; // "GEMINI_IA", "SISTEMA_NET", "SISTEMA_JAVA"
}
