package com.example.lyra.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DotNetHumorRequest {

    @JsonProperty("ResumoRecebido")
    private String resumo;
    @JsonProperty("Nivel")
    private Integer nivelGravidade;
    private String descricaoOriginal;
    @JsonProperty("Prioridade")
    private Boolean prioridade;
}
