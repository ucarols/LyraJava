package com.example.lyra.dto.request;

import com.example.lyra.model.EHumor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HumorAnalysisRequest {
    
    @NotNull(message = "O nível de humor original é obrigatório")
    private EHumor nivelHumorOriginal;
    
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 2000, message = "A descrição deve ter entre 10 e 2000 caracteres")
    private String descricao;
}
