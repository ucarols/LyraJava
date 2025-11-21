package com.example.lyra.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DotNetHumorRequest {
    
    private String resumo;
    private Integer nivelGravidade;
    private String descricaoOriginal;
    private Boolean prioridade;
}
