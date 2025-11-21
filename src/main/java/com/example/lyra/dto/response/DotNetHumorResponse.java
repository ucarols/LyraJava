package com.example.lyra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DotNetHumorResponse {
    
    private String mensagem;
    private String orientacao;
    private String recursosSugeridos;
    private Boolean sucessoProcessamento;
}
